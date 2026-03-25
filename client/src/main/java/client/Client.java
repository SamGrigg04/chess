package client;

/*
handle commands like help, register, join game, quit, etc.
call ServerFacade for backend work
call the renderer when a player tries to play/observe
 */

import java.util.Scanner;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
//import client.websocket.NotificationHandler;
import server.ServerFacade;
//import client.websocket.WebSocketFacade;
//import webSocketMessages.Notification;

public class Client {
    private String visitorName = null;
    private String authToken = null;
    private final ServerFacade server;
//    private final WebSocketFacade ws;
    private State state = State.SIGNEDOUT;

    public Client(String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);
//        ws = new WebSocketFacade(serverUrl, this);
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
                System.out.print(ex.getMessage());
            }
        }
        System.out.println();
    }

//    public void notify(Notification notification) {
//        System.out.println("\u001b[31m" + notification.message());
//        printPrompt();
//    }

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
            case 9 -> joinGame(collectInputs(scanner, "Game ID: "));
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

        AuthData authData = server.login(params[0], params[1]);
        state = State.SIGNEDIN;
        authToken = authData.authToken();
        visitorName = authData.username();
//        ws.enterPetShop(visitorName);
        return String.format("You signed in as %s.", visitorName);
    }

    public String register(String... params) throws ResponseException {
        if (params.length < 3) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected username, password, and email");
        }

        AuthData authData = server.register(params[0], params[1], params[2]);
        state = State.SIGNEDIN;
        authToken = authData.authToken();
        visitorName = authData.username();

        return String.format("You registered as %s", visitorName);
    }

    public String logout() throws ResponseException {
        return null;
    }

    public String createGame(String... params) throws ResponseException {
        return null;
    }

    public String listGames() throws ResponseException {
        return null;
    }

    public String joinGame(String... params) throws ResponseException {
        return null;
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
                Input 1 - help
                Input 4 - quit
                Input 5 - logout
                Input 6 - create game
                Input 7 - list games
                Input 8 - play game
                Input 9 - observe game
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
