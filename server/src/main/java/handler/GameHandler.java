package handler;

import Request.CreateRequest;
import Request.JoinRequest;
import result.CreateResult;
import result.ListResult;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.GameData;
import service.AlreadyTakenException;
import service.GameService;
import service.UnauthorizedException;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        String authToken = ctx.header("authorization");

        ListResult result;
        try {
            result = gameService.listGames(authToken);
        } catch (UnauthorizedException e) {
            message.put("message", "Error: unauthorized");
            ctx.status(401).json(message);
            return;
        } catch (DataAccessException e) {
            message.put("message", "internal DAO failure");
            ctx.status(500).json(message);
            return;
        }

        HashMap<String, ArrayList<GameData>> message2 = new HashMap<>();
        message2.put("games", (ArrayList<GameData>) result.games());
        ctx.status(200).json(message2);
    }

    public void createGame(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        String authToken = ctx.header("authorization");
        CreateRequest body = ctx.bodyAsClass(CreateRequest.class);

        if (body.gameName() == null || body.gameName().isEmpty()) {
            message.put("message", "Error: bad request");
            ctx.status(400).json(message);
            return;
        }

        CreateResult result;
        try {
            result = gameService.createGame(body, authToken);
        } catch (UnauthorizedException e) {
            message.put("message", "Error: unauthorized");
            ctx.status(401).json(message);
            return;
        } catch (DataAccessException e) {
            message.put("message", "internal DAO failure");
            ctx.status(500).json(message);
            return;
        }

        HashMap<String, Integer> message2 = new HashMap<>();
        message2.put("gameID", result.gameID());
        ctx.status(200).json(message2);
    }

    public void joinGame(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        String authToken = ctx.header("authorization");
        JoinRequest body = ctx.bodyAsClass(JoinRequest.class);

        if (body.playerColor() == null || body.gameID() == null || body.playerColor().isEmpty()) {
            message.put("message", "Error: bad request");
            ctx.status(400).json(message);
            return;
        }

        CreateResult result;
        try {
            result = gameService.joinGame(body, authToken);
        } catch (UnauthorizedException e) {
            message.put("message", "Error: unauthorized");
            ctx.status(401).json(message);
            return;
        } catch (AlreadyTakenException e) {
            message.put("message", "Error: already taken");
            ctx.status(403).json(message);
            return;
        } catch (DataAccessException e) {
            message.put("message", "internal DAO failure");
            ctx.status(500).json(message);
            return;
        }

        HashMap<String, Integer> message2 = new HashMap<>();
        message2.put("gameID", result.gameID());
        ctx.status(200).json(message2);
    }

}
