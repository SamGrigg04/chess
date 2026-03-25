package client;

import java.util.Collection;
import java.util.Scanner;

import exception.ResponseException;
import model.GameData;
import result.AuthResult;
import serverFacade.ServerFacade;
import ui.ChessBoardRenderer;


public class Client {
    private String visitorName = null;
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public Client(String serverURL) {
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
            } catch (Exception ex) { // Catch any other random errors
                // If there's a message, display it. If not, display the class of the error
                var msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
                System.out.print("Something went wrong: " + msg + "\n");
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
            case 8 -> joinGame(scanner, collectInputs(scanner, "Game ID: ", "Player color (white/black): "));
            case 9 -> observeGame(scanner, collectInputs(scanner, "Game ID: "));
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
            throw new ResponseException("Expected username and password");
        }

        AuthResult authResult = server.login(params[0], params[1]);
        state = State.SIGNEDIN;
        authToken = authResult.authToken();
        visitorName = authResult.username();
        return String.format("You signed in as %s.", visitorName);
    }

    public String register(String... params) throws ResponseException {
        if (params.length < 3) {
            throw new ResponseException("Expected username, password, and email");
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
            throw new ResponseException("Expected gameID");
        }

        String gameName = params[0].trim();
        if (gameName.isEmpty()) {
            throw new ResponseException("Game name cannot be blank");
        }

        int gameID = server.createGame(gameName, authToken);

        return String.format("Created game with id %s", gameID);
    }

    public String joinGame(Scanner scanner, String... params) throws ResponseException {
        assertSignedIn();

        if (params.length < 2) {
            throw new ResponseException("Expected gameID and player color");
        }

        int gameID;
        try {
            gameID = Integer.parseInt(params[0]);
        } catch (NumberFormatException ex) {
            throw new ResponseException("Game ID must be a number");
        }
        var playerColorInput = params[1].trim();
        ChessBoardRenderer.PlayerColor color;
        try {
            color = ChessBoardRenderer.PlayerColor.valueOf(playerColorInput.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseException("Player color must be white or black");
        }
        server.joinGame(color.name(), gameID, authToken);

        ChessBoardRenderer.render(color);
        waitForEnter(scanner);

        return String.format("Joined game with id %s ", params[0]);
    }

    public String observeGame(Scanner scanner, String... params) throws ResponseException {
        assertSignedIn();

        if (params.length < 1) {
            throw new ResponseException("Expected gameID");
        }
        String[] newParams = new String[2];
        newParams[0] = params[0];
        newParams[1] = "WHITE";
        joinGame(scanner, newParams);

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

    private void waitForEnter(Scanner scanner) {
        System.out.print("\u001b[0m" + "Press Enter to proceed...");
        scanner.nextLine();
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }
}
