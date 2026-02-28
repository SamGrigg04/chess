package server;

import io.javalin.*;
import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        var userHandler = new UserHandler();
        var gameHandler = new GameHandler();
        var clearHandler = new ClearHandler();


        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

        javalin.delete("/db", clearHandler::clear);

        javalin.exception(Exception.class, (e, ctx) ->
                ctx.status(500).json(new ErrorResponse("Error: " + e.getMessage())));

        // Register your endpoints and exception handlers here.

    }

    private record ErrorResponse(String message) {}

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
