package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

public class LogoutTests {
    @Test
    void logoutSuccess() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var userDAO = new MemoryUserDAO();
        var userService = new UserService(authDAO, userDAO);

        var registerResult = userService.register(new RegisterRequest("hehehe", "pw", "b@mail.com"));
        String authToken = registerResult.authToken();

        userService.logout(authToken);

        Assertions.assertNull(authDAO.getAuth(authToken));
    }

    @Test
    void logoutUnauthorized() {
        var authDAO = new MemoryAuthDAO();
        var userDAO = new MemoryUserDAO();
        var userService = new UserService(authDAO, userDAO);

        Assertions.assertThrows(UnauthorizedException.class,
                () -> userService.logout("superLameToken"));
    }
}
