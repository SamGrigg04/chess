package handler;

import io.javalin.http.Context;
import service.GameService;

import java.util.HashMap;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    // All the .json(X) should be hashMaps
    public void listGames(Context ctx) {
        gameService.listGames();

        String gameList = "temp";
        ctx.status(200).json(gameList);
    }

    public void joinGame(Context ctx) {

        ctx.status(200).json(new HashMap<>());
    }

    public void createGame(Context ctx) {

        int gameID = 1234;
        ctx.status(200).json(gameID);
    }

}
