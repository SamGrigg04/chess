package client;

/*
handle commands like help, register, join game, quit, etc.
call ServerFacade for backend work
call the renderer when a player tries to play/observe
 */

import java.util.Collection;
import java.util.Scanner;

import exception.ResponseException;
import model.GameData;
import result.AuthResult;
import server.ServerFacade; // TODO: code quality doesn't like this, but I need it right?
import ui.ChessBoardRenderer;


public class Client {
    private String visitorName = null;
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public Client(String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);
    }

    public void run() {
        System.out.println("Welcome to chess I guess. Sign in to start." + "\n");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            // Thanks, CS 260. Neat syntax no?
            String menu = (state == State.SIGNEDOUT) ? signedOutMenu() : signedInMenu();
            System.out.print(menu);
            printPrompt();
            String line = scanner.nextLine();

            int option = validateOption(line);
            if (option == -1) {
                System.out.print("\u001b[31m" + "Please select a valid option number." + "\u001b[0m" + "\n");
                continue;
            }

            try {
                String actionResult;
                if (state == State.SIGNEDOUT) {
                    actionResult = loggedOutEval(option, scanner);
                } else {
                    actionResult = loggedInEval(option, scanner);
                }
                if ("quit".equalsIgnoreCase(actionResult)) {
                    running = false;
                } else if (actionResult != null && !actionResult.isBlank()) {
                    System.out.println("\u001b[0m" + actionResult);
                }
            } catch (ResponseException ex) {
                System.out.print(ex.getMessage() + "\n");
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + "\u001b[0m" + ">>> ");
    }

    private int validateOption(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private String loggedOutEval(int option, Scanner scanner) throws ResponseException {
        return switch (option) {
            case 1 -> signedOutHelp();
            case 2 -> login(collectInputs(scanner, "Username: ", "Password: "));
            case 3 -> register(collectInputs(scanner, "Username: ", "Password: ", "Email: "));
            case 4 -> "quit";
            default -> "Please select a valid option";
        };
    }

    private String loggedInEval(int option, Scanner scanner) throws ResponseException {
        return switch (option) {
            case 1 -> signedInHelp();
            case 4 -> "quit";
            case 5 -> logout();
            case 6 -> createGame(collectInputs(scanner, "Game name: "));
            case 7 -> listGames();
            case 8 -> joinGame(collectInputs(scanner, "Game ID: ", "Player color (white/black): "));
            case 9 -> observeGame(collectInputs(scanner, "Game ID: "));
            default -> "Please select a valid option";
        };
    }

    private String[] collectInputs(Scanner scanner, String... prompts) {
        String[] responses = new String[prompts.length];
        for (int i = 0; i < prompts.length; i++) {
            System.out.print(prompts[i]);
            responses[i] = scanner.nextLine();
        }
        return responses;
    }

    public String login(String... params) throws ResponseException {
        if (params.length < 2) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected username and password");
        }

        AuthResult authResult = server.login(params[0], params[1]);
        state = State.SIGNEDIN;
        authToken = authResult.authToken();
        visitorName = authResult.username();
        return String.format("You signed in as %s.", visitorName);
    }

    public String register(String... params) throws ResponseException {
        if (params.length < 3) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected username, password, and email");
        }

        AuthResult authResult = server.register(params[0], params[1], params[2]);
        state = State.SIGNEDIN;
        authToken = authResult.authToken();
        visitorName = authResult.username();

        return String.format("You registered as %s", visitorName);
    }

    public String logout() throws ResponseException {
        assertSignedIn();

        server.logout(authToken);
        state = State.SIGNEDOUT;

        return String.format("%s understandably got bored of chess and left", visitorName);
    }

    public String listGames() throws ResponseException {
        assertSignedIn();

        Collection<GameData> gameList = server.listGames(authToken);
        if (gameList.isEmpty()) {
            return "You are alone in this universe. \n";
        }
        StringBuilder outString = new StringBuilder();

        int gameNumber = 1;
        for (GameData game : gameList) {
            outString
                    .append(gameNumber++)
                    .append(". ")
                    .append(game.gameName())
                    .append(" | white: ")
                    .append(game.whiteUsername() == null ? "empty" : game.whiteUsername())
                    .append(" | black: ")
                    .append(game.blackUsername() == null ? "empty" : game.blackUsername())
                    .append(" | id: ")
                    .append(game.gameID())
                    .append("\n");
        }

        return outString.toString();
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();

        if (params.length < 1) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected gameID");
        }

        int gameID = server.createGame(params[0], authToken);

        return String.format("Created game with id %s", gameID);
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();

        if (params.length < 2) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected gameID and player color");
        }

        int gameID = Integer.parseInt(params[0]);
        var playerColor = params[1].toUpperCase();
        server.joinGame(playerColor, gameID, authToken);

        ChessBoardRenderer.render(ChessBoardRenderer.PlayerColor.valueOf(playerColor));

        return String.format("Joined game with id %s ", params[0]);
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();

        if (params.length < 1) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected gameID");
        }

        ChessBoardRenderer.render(ChessBoardRenderer.PlayerColor.WHITE);

        return String.format("Observing game with id %s ", params[0]);
    }

    private String signedOutMenu() {
        return """
                1 - help
                2 - login
                3 - register
                4 - quit
                """;
    }

    private String signedInMenu() {
        return """
                1 - help
                4 - quit
                5 - logout
                6 - create game
                7 - list games
                8 - play game
                9 - observe game
                """;
    }

    private String signedOutHelp() {
        return """
                Input 1 for help
                Input 2 to go to the login screen
                Input 3 to register a new account
                Input 4 to quit
                """;
    }

    private String signedInHelp() {
        return """
                Input 1 for help
                Input 4 to quit
                Input 5 to logout
                Input 6 to create a new game
                Input 7 to list all games that currently exist
                Input 8 to play an existing game
                Input 9 to observe an existing game
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }
}
