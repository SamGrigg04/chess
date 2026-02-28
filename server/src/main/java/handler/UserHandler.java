package handler;

import Request.*;
import Result.AuthResult;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.AlreadyTakenException;
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
        boolean nullFields = body.username() == null || body.password() == null || body.email() == null;
        if (emptyFields || nullFields) {
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

        String temp = "Username, authToken";
        ctx.status(200).json(temp);
    }

    public void logout(Context ctx) {

        ctx.status(200).json(new HashMap<>());
    }

}
