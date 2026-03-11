package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTests {

    @Test
    void clearSuccess() throws Exception {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var userDAO = new MemoryUserDAO();

        userDAO.createUser("alice", "pw", "a@mail.com");
        authDAO.createAuth("supersecrettoken", "alice");

        var clearService = new ClearService(authDAO, gameDAO, userDAO);
        clearService.clear();

        assertNull(userDAO.getUser("alice"));
        assertNull(authDAO.getAuth("supersecrettoken"));
    }

    @Test
    void clearEmpty() throws DataAccessException {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var userDAO = new MemoryUserDAO();

        var clearService = new ClearService(authDAO, gameDAO, userDAO);
        clearService.clear();

        assertDoesNotThrow(clearService::clear);
    }
}
