package handler;

import Request.*;
import result.AuthResult;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.AlreadyTakenException;
import service.UnauthorizedException;
import service.UserService;

import java.util.HashMap;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        RegisterRequest body = ctx.bodyAsClass(RegisterRequest.class);

        boolean emptyFields = body.username().isEmpty() || body.password().isEmpty() || body.email().isEmpty();
        if (emptyFields) {
            message.put("message", "Error: bad request");
            ctx.status(400).json(message);
            return;
        }

        AuthResult result;
        try {
            result = userService.register(body);
        } catch (AlreadyTakenException e) {
            message.put("message", "Error: already taken");
            ctx.status(403).json(message);
            return;
        } catch (DataAccessException e) {
            message.put("message", "internal DAO failure");
            ctx.status(500).json(message);
            return;
        }

        message.put("username", body.username());
        message.put("authToken", result.authToken());
        ctx.status(200).json(message);
    }

    public void login(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        LoginRequest body = ctx.bodyAsClass(LoginRequest.class);

        boolean emptyFields = body.username().isEmpty() || body.password().isEmpty();
        if (emptyFields) {
            message.put("message", "Error: bad request");
            ctx.status(400).json(message);
            return;
        }

        AuthResult result;
        try {
            result = userService.login(body);
        } catch (UnauthorizedException e) {
            message.put("message", "Error: unauthorized");
            ctx.status(401).json(message);
            return;
        } catch (DataAccessException e) {
            message.put("message", "internal DAO failure");
            ctx.status(500).json(message);
            return;
        }

            message.put("username", body.username());
            message.put("authToken", result.authToken());
            ctx.status(200).json(message);
    }

    public void logout(Context ctx) {
        HashMap<String, String> message = new HashMap<>();
        String authToken = ctx.header("authorization");

        try {
            userService.logout(authToken);
        } catch (UnauthorizedException e) {
            message.put("message", "Error: unauthorized");
            ctx.status(401).json(message);
            return;
        } catch (DataAccessException e) {
            message.put("message", "internal DAO failure");
            ctx.status(500).json(message);
            return;
        }

        ctx.status(200).json(message);
    }

}
