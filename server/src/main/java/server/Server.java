package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.*;
import io.javalin.Javalin;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {

        // CHANGE THIS VALUE TO SWITCH FROM LOCAL TO DATABASE
        boolean useLocalStorage = false;

        UserHandler userHandler;
        GameHandler gameHandler;
        ClearHandler clearHandler;

        if (useLocalStorage) {
            MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
            MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
            MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

            userHandler = new UserHandler(new UserService(memoryAuthDAO, memoryUserDAO));
            gameHandler = new GameHandler(new GameService(memoryAuthDAO, memoryGameDAO));
            clearHandler = new ClearHandler(new ClearService(memoryAuthDAO, memoryGameDAO, memoryUserDAO));
        } else {
            MySqlAuthDAO mySqlAuthDAO = new MySqlAuthDAO();
            MySqlGameDAO mySqlGameDAO = new MySqlGameDAO();
            MySqlUserDAO mySqlUserDAO = new MySqlUserDAO();

            userHandler = new UserHandler(new UserService(mySqlAuthDAO, mySqlUserDAO));
            gameHandler = new GameHandler(new GameService(mySqlAuthDAO, mySqlGameDAO));
            clearHandler = new ClearHandler(new ClearService(mySqlAuthDAO, mySqlGameDAO, mySqlUserDAO));

            //TODO: Create database and tables here? Probably call methods from Database Manager
        }

        var serializer = new Gson();

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

        javalin.delete("/db", clearHandler::clear);

        javalin.exception(Exception.class, (e, ctx) ->
                ctx.status(500).result(serializer.toJson(Map.of("message", "Error: " + e.getMessage()))));
        javalin.exception(AlreadyTakenException.class, (e, ctx) ->
                ctx.status(403).result(serializer.toJson(Map.of("message", "Error: already taken"))));
        javalin.exception(UnauthorizedException.class, (e, ctx) ->
                ctx.status(401).result(serializer.toJson(Map.of("message", "Error: unauthorized"))));
        javalin.exception(NoGameException.class, (e, ctx) ->
                ctx.status(400).result(serializer.toJson(Map.of("message", "Error: bad request"))));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
