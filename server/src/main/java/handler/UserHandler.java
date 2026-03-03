package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import request.LoginRequest;
import request.RegisterRequest;
import result.AuthResult;
import service.AlreadyTakenException;
import service.UnauthorizedException;
import service.UserService;

import java.util.Map;

public class UserHandler {
    private static final Gson SERIALIZER = new Gson();
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) {
        RegisterRequest body = SERIALIZER.fromJson(ctx.body(), RegisterRequest.class);

        boolean bad = (body == null || body.username() == null || body.password() == null || body.email() == null
                || body.username().isEmpty() || body.password().isEmpty() || body.email().isEmpty());
        if (bad) {
            ctx.status(400).result(SERIALIZER.toJson(Map.of("message", "Error: bad request")));
            return;
        }

        try {
            AuthResult result = userService.register(body);
            ctx.status(200).result(SERIALIZER.toJson(Map.of(
                    "username", result.username(),
                    "authToken", result.authToken())));
        } catch (AlreadyTakenException e) {
            ctx.status(403).result(SERIALIZER.toJson(Map.of("message", "Error: already taken")));
        } catch (DataAccessException e) {
            ctx.status(500).result(SERIALIZER.toJson(Map.of("message", "Error: internal server error")));
        }
    }

    public void login(Context ctx) {
        LoginRequest body = SERIALIZER.fromJson(ctx.body(), LoginRequest.class);

        boolean bad = (body == null || body.username() == null || body.password() == null
                || body.username().isEmpty() || body.password().isEmpty());
        if (bad) {
            ctx.status(400).result(SERIALIZER.toJson(Map.of("message", "Error: bad request")));
            return;
        }

        try {
            AuthResult result = userService.login(body);
            ctx.status(200).result(SERIALIZER.toJson(Map.of(
                    "username", result.username(),
                    "authToken", result.authToken())));
        } catch (UnauthorizedException e) {
            ctx.status(401).result(SERIALIZER.toJson(Map.of("message", "Error: unauthorized")));
        } catch (DataAccessException e) {
            ctx.status(500).result(SERIALIZER.toJson(Map.of("message", "Error: internal server error")));
        }
    }

    public void logout(Context ctx) {
        String authToken = ctx.header("authorization");

        try {
            userService.logout(authToken);
            ctx.status(200).result(SERIALIZER.toJson(Map.of()));
        } catch (UnauthorizedException e) {
            ctx.status(401).result(SERIALIZER.toJson(Map.of("message", "Error: unauthorized")));
        } catch (DataAccessException e) {
            ctx.status(500).result(SERIALIZER.toJson(Map.of("message", "Error: internal server error")));
        }
    }
}
