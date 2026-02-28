package handler;

import io.javalin.http.Context;
import service.UserService;

import java.util.HashMap;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    // All the .json(X) should be hashMaps
    public void register(Context ctx) {

        String temp = "Username, authToken";
        ctx.status(200).json(temp);
    }

    public void login(Context ctx) {

        String temp = "Username, authToken";
        ctx.status(200).json(temp);
    }

    public void logout(Context ctx) {

        ctx.status(200).json(new HashMap<>());
    }

}
