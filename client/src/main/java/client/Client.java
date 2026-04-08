package client;

import java.util.Collection;
import java.util.Scanner;

import chess.ChessBoard;
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
            case 2 -> "quit";
            case 3 -> logout();
            case 4 -> createGame(collectInputs(scanner, "Game name: "));
            case 5 -> listGames();
            case 6 -> joinGame(false, scanner, collectInputs(scanner, "Game ID: ", "Player color (white/black): "));
            case 7 -> observeGame(scanner, collectInputs(scanner, "Game ID: "));
            default -> "Please select a valid option";
        };
    }

    // TODO: implement functions
    private String playingEval(int option, Scanner scanner) throws ResponseException {
        return switch (option) {
            case 1 -> playHelp();
            case 2 -> redrawBoard();
            case 3 -> leaveGame();
            case 4 -> makeMove();
            case 5 -> resign();
            case 6 -> highlightMoves();
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

    // TODO: call the join endpoint, open a websocket connection, send a CONNECT message, transition to gameplay UI
    public String joinGame(boolean isObserver, Scanner scanner, String... params) throws ResponseException {
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

        if (!isObserver) {
            server.joinGame(color.name(), gameID, authToken);
        }

        ChessBoard board = findGame(gameID).game().getBoard();
        ChessBoardRenderer.render(board, color, false);
        waitForEnter(scanner);

        return String.format("Joined game with id %s ", params[0]);
    }

    // TODO: do not call the join endpoint, open a websocket connection, send a CONNECT message, transition to gameplay UI
    public String observeGame(Scanner scanner, String... params) throws ResponseException {
        assertSignedIn();

        if (params.length < 1) {
            throw new ResponseException("Expected gameID");
        }

        String[] newParams = new String[2];
        newParams[0] = params[0];
        newParams[1] = "WHITE";
        joinGame(true, scanner, newParams);

        return String.format("Observing game with id %s ", params[0]);

    }

    private String highlightMoves() {
        return "";
    }

    private String resign() {
        return "";
    }

    private String makeMove() {
        return "";
    }

    private String leaveGame() {
        return "";
    }

    private String redrawBoard() {
        return "";
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
                2 - quit
                3 - logout
                4 - create game
                5 - list games
                6 - play game
                7 - observe game
                """;
    }

    private String playMenu() {
        return """
                1 - help
                2 - redraw board
                3 - leave
                4 - make move
                5 - resign
                6 - highlight legal moves
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

    private String playHelp() {
        return """
                Input 1 for help
                Input 2 to redraw the chess board
                Input 3 to leave the game. Leaving the game does not cause you to forfeit. Another player may join \n
                the game at any time and continue where you left off.
                Input 4 to make a move. You can only move during your turn. Observers cannot make moves. Moves must \n
                be in chess notation.
                Input 5 to resign. Resigning results in an automatic forfeit. Observers cannot resign. Resigning \n
                does not cause you to leave the game.
                Input 6 to highlight legal moves for any given piece. Location is specified \n
                by <row number><column number> (e.g. 1a).
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

    private GameData findGame(int gameID) throws ResponseException {
        for (GameData game : server.listGames(authToken)) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new ResponseException("Game not found");
    }
}
