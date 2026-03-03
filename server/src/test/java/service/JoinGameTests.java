package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.JoinRequest;
import request.RegisterRequest;

public class JoinGameTests {
    @Test
    void joinGameSuccess() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var userDAO = new MemoryUserDAO();

        var userService = new UserService(authDAO, userDAO);
        var gameService = new GameService(authDAO, gameDAO);

        var registerResult = userService.register(new RegisterRequest("carl", "pw", "c@mail.com"));
        String authToken = registerResult.authToken();

        var createResult = gameService.createGame(new CreateRequest("chess is for nerds"), authToken);
        gameService.joinGame(new JoinRequest("WHITE", createResult.gameID()), authToken);

        GameData updated = gameDAO.getGame(createResult.gameID());
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("carl", updated.whiteUsername());
    }

    @Test
    void joinGameBadRequest() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var userDAO = new MemoryUserDAO();

        var userService = new UserService(authDAO, userDAO);
        var gameService = new GameService(authDAO, gameDAO);

        var registerResult = userService.register(new RegisterRequest("barl", "pw", "d@mail.com"));
        String authToken = registerResult.authToken();

        var createResult = gameService.createGame(new CreateRequest("chess is still for nerds"), authToken);

        Assertions.assertThrows(NoGameException.class,
                () -> gameService.joinGame(new JoinRequest("GREEN", createResult.gameID()), authToken));
    }
}
