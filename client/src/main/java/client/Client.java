package client;


/*
handle commands like help, register, join game, quit, etc.
call ServerFacade for backend work
call the renderer when a player tries to play/observe
 */

import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
//import client.websocket.NotificationHandler;
import server.ServerFacade;
//import client.websocket.WebSocketFacade;
//import webSocketMessages.Notification;

public class Client {
    private String visitorName = null;
    private final ServerFacade server;
//    private final WebSocketFacade ws;
    private State state = State.SIGNEDOUT;

    public Client(String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);
//        ws = new WebSocketFacade(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to chess I guess. Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print("\u001b[34m" + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

//    public void notify(Notification notification) {
//        System.out.println("\u001b[31m" + notification.message());
//        printPrompt();
//    }

    private void printPrompt() {
        System.out.print("\n" + "\u001b[0m" + ">>> " + "\u001b[32m");
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create game" -> createGame(params);
                case "list games" -> listGames();
                case "play game", "observe game" -> joinGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
//            ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <yourname>");
    }

    public String register(String... params) throws ResponseException {
        return null;
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

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    2 - login
                    3 - register
                    4 - quit
                    """;
        }
        return """
                2 - logout
                3 - create game
                4 - list games
                5 - play game
                6 - join game
                7 - observe game
                8 - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }
}