package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.RegisterRequest;

public class CreateGameTests {
    @Test
    void createGameSuccess() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var userDAO = new MemoryUserDAO();

        var userService = new UserService(authDAO, userDAO);
        var gameService = new GameService(authDAO, gameDAO);

        var registerResult = userService.register(new RegisterRequest("heyLookAUsername", "pw", "b@mail.com"));
        String authToken = registerResult.authToken();

        var createResult = gameService.createGame(new CreateRequest("creative game name"), authToken);

        Assertions.assertNotNull(createResult);
        Assertions.assertTrue(createResult.gameID() > 0);
        Assertions.assertNotNull(gameDAO.getGame(createResult.gameID()));
    }

    @Test
    void createGameUnauthorized() {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var gameService = new GameService(authDAO, gameDAO);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> gameService.createGame(new CreateRequest("creative game name"), "susToken"));
    }
}
