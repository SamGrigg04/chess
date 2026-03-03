package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import request.CreateRequest;
import request.JoinRequest;
import result.CreateResult;
import result.ListResult;
import service.AlreadyTakenException;
import service.GameService;
import service.NoGameException;
import service.UnauthorizedException;

import java.util.Map;

public class GameHandler {
    private static final Gson Serializer = new Gson();
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGames(Context ctx) {
        String authToken = ctx.header("authorization");

        try {
            ListResult result = gameService.listGames(authToken);
            ctx.status(200).result(Serializer.toJson(Map.of("games", result.games())));
        } catch (UnauthorizedException e) {
            ctx.status(401).result(Serializer.toJson(Map.of("message", "Error: unauthorized")));
        } catch (DataAccessException e) {
            ctx.status(500).result(Serializer.toJson(Map.of("message", "Error: internal server error")));
        }
    }

    public void createGame(Context ctx) {
        String authToken = ctx.header("authorization");
        CreateRequest body = Serializer.fromJson(ctx.body(), CreateRequest.class);

        boolean bad = (body == null || body.gameName() == null || body.gameName().isEmpty());
        if (bad) {
            ctx.status(400).result(Serializer.toJson(Map.of("message", "Error: bad request")));
            return;
        }

        try {
            CreateResult result = gameService.createGame(body, authToken);
            ctx.status(200).result(Serializer.toJson(Map.of("gameID", result.gameID())));
        } catch (UnauthorizedException e) {
            ctx.status(401).result(Serializer.toJson(Map.of("message", "Error: unauthorized")));
        } catch (DataAccessException e) {
            ctx.status(500).result(Serializer.toJson(Map.of("message", "Error: internal server error")));
        }
    }

    public void joinGame(Context ctx) {
        String authToken = ctx.header("authorization");
        JoinRequest body = Serializer.fromJson(ctx.body(), JoinRequest.class);

        boolean bad = (body == null || body.playerColor() == null || body.gameID() == null || body.playerColor().isEmpty());
        if (bad) {
            ctx.status(400).result(Serializer.toJson(Map.of("message", "Error: bad request")));
            return;
        }

        try {
            gameService.joinGame(body, authToken);
            ctx.status(200).result(Serializer.toJson(Map.of()));
        } catch (NoGameException e) {
            ctx.status(400).result(Serializer.toJson(Map.of("message", "Error: bad request")));
        } catch (UnauthorizedException e) {
            ctx.status(401).result(Serializer.toJson(Map.of("message", "Error: unauthorized")));
        } catch (AlreadyTakenException e) {
            ctx.status(403).result(Serializer.toJson(Map.of("message", "Error: already taken")));
        } catch (DataAccessException e) {
            ctx.status(500).result(Serializer.toJson(Map.of("message", "Error: internal server error")));
        }
    }
}
