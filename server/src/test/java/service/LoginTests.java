package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;

public class LoginTests {
    @Test
    void loginSuccess() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var userDAO = new MemoryUserDAO();
        var userService = new UserService(authDAO, userDAO);

        userService.register(new RegisterRequest("sam", "pw", "a@mail.com"));
        var result = userService.login(new LoginRequest("sam", "pw"));

        Assertions.assertNotNull(result);
        Assertions.assertEquals("sam", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertNotNull(authDAO.getAuth(result.authToken()));
    }

    @Test
    void loginUnauthorized() {
        var authDAO = new MemoryAuthDAO();
        var userDAO = new MemoryUserDAO();
        var userService = new UserService(authDAO, userDAO);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> userService.login(new LoginRequest("missing:(", "pw")));
    }
}
