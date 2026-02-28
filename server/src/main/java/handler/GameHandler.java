package handler;

import Result.ListResult;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.GameData;
import service.GameService;
import service.UnauthorizedException;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    // All the .json(X) should be hashMaps
    public void listGames(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        String authToken = ctx.header("authorization");

        ListResult listResult;
        try {
            listResult = gameService.listGames(authToken);
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
        message2.put("games", (ArrayList<GameData>) listResult.games());
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

        int gameID = 1234;
        ctx.status(200).json(gameID);
    }

    public void joinGame(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        String authToken = ctx.header("authorization");


        ctx.status(200).json(message);
    }

}
