package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.AlreadyTakenException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlUserDAOTest {
    private static MySqlUserDAO userDAO;
    private static MySqlAuthDAO authDAO;
    private static MySqlGameDAO gameDAO;

    @BeforeAll
    static void setUpTables() throws DataAccessException {
        userDAO = new MySqlUserDAO();
        authDAO = new MySqlAuthDAO();
        gameDAO = new MySqlGameDAO();

        userDAO.setupUserTable();
        gameDAO.setupGameTable();
        authDAO.setupAuthTable();
    }

    @BeforeEach
    void cleanDatabase() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    void createPositive() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUsername();
        var password = "Password!123";
        var email = uniqueEmail();

        userDAO.createUser(username, password, email);

        UserData retrieved = userDAO.getUser(username);
        assertNotNull(retrieved);
        assertEquals(username, retrieved.username());
        assertEquals(email, retrieved.email());
        assertTrue(BCrypt.checkpw(password, retrieved.password()));
    }

    @Test
    void createUserNegative() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUsername();
        userDAO.createUser(username, "pw", uniqueEmail());

        assertThrows(DataAccessException.class, () -> userDAO.createUser(username, "pw", uniqueEmail()));
    }

    @Test
    void getUserPositive() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUsername();
        userDAO.createUser(username, "pw", uniqueEmail());

        assertNotNull(userDAO.getUser(username));
    }

    @Test
    void getUserNegative() throws DataAccessException {
        assertNull(userDAO.getUser(UUID.randomUUID().toString()));
    }

    @Test
    void clearTest() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUsername();
        userDAO.createUser(username, "pw", uniqueEmail());

        userDAO.clear();
        assertNull(userDAO.getUser(username));
    }

    @Test
    void setupUserTableTest() {
        assertDoesNotThrow(userDAO::setupUserTable);
    }

    private static String uniqueUsername() {
        return "sql_user_" + UUID.randomUUID();
    }

    private static String uniqueEmail() {
        return "sql+" + UUID.randomUUID() + "@example.com";
    }
}
