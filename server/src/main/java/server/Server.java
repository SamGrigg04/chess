package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.*;
import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

        var userHandler = new UserHandler(new UserService(memoryAuthDAO, memoryUserDAO));
        var gameHandler = new GameHandler(new GameService(memoryAuthDAO, memoryGameDAO));
        var clearHandler = new ClearHandler(new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO));


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
        javalin.exception(AlreadyTakenException.class, (e, ctx) ->
                ctx.status(403).json(new ErrorResponse(e.getMessage())));
        javalin.exception(UnauthorizedException.class, (e, ctx) ->
                ctx.status(401).json(new ErrorResponse(e.getMessage())));

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
