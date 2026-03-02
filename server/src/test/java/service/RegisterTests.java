package service;

import Request.RegisterRequest;
import result.AuthResult;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterTests {

    @Test
    void registerSuccess() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var userDAO = new MemoryUserDAO();

        var userService = new UserService(authDAO, userDAO);

        AuthResult result = userService.register(new RegisterRequest("bob", "psw", "b@mail.com"));

        Assertions.assertNotNull(result);
        Assertions.assertEquals("bob", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertNotNull(userDAO.getUser("bob"));
        Assertions.assertNotNull(authDAO.getAuth(result.authToken()));
    }

    @Test
    void usernameAlreadyTaken() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var userDAO = new MemoryUserDAO();

        var userService = new UserService(authDAO, userDAO);
        userService.register(new RegisterRequest("taken", "pw", "t@mail.com"));

        Assertions.assertThrows(AlreadyTakenException.class,
                () -> userService.register(new RegisterRequest("taken", "wp", "u@mail.com")));
    }
}
