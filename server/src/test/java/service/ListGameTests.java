package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.RegisterRequest;

public class ListGameTests {
    @Test
    void listGamesSuccess() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var userDAO = new MemoryUserDAO();

        var userService = new UserService(authDAO, userDAO);
        var gameService = new GameService(authDAO, gameDAO);

        var registerResult = userService.register(new RegisterRequest("goober", "pw", "a@mail.com"));
        String authToken = registerResult.authToken();

        gameService.createGame(new CreateRequest("test game"), authToken);
        var listResult = gameService.listGames(authToken);

        Assertions.assertNotNull(listResult);
        Assertions.assertNotNull(listResult.games());
        Assertions.assertEquals(1, listResult.games().size());
    }

    @Test
    void listGamesUnauthorized() {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var gameService = new GameService(authDAO, gameDAO);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> gameService.listGames("uglyToken"));
    }
}
