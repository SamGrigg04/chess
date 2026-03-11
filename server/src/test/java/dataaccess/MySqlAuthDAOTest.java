package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AlreadyTakenException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlAuthDAOTest {
    private static MySqlAuthDAO authDAO;
    private static MySqlUserDAO userDAO;
    private static MySqlGameDAO gameDAO;

    @BeforeAll
    static void setUpTables() throws DataAccessException {
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();
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
    void createAuthPositive() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUser();
        userDAO.createUser(username, "pw", uniqueEmail());
        var token = UUID.randomUUID().toString();

        authDAO.createAuth(token, username);

        AuthData retrieved = authDAO.getAuth(token);
        assertNotNull(retrieved);
        assertEquals(username, retrieved.username());
    }

    @Test
    void createAuthNegative() {
        var token = UUID.randomUUID().toString();
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(token, "missing"));
    }

    @Test
    void getAuthPositive() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUser();
        userDAO.createUser(username, "pw", uniqueEmail());
        var token = UUID.randomUUID().toString();
        authDAO.createAuth(token, username);

        AuthData retrieved = authDAO.getAuth(token);
        assertNotNull(retrieved);
        assertEquals(token, retrieved.authToken());
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        assertNull(authDAO.getAuth(UUID.randomUUID().toString()));
    }

    @Test
    void deleteAuthPositive() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUser();
        userDAO.createUser(username, "pw", uniqueEmail());
        var token = UUID.randomUUID().toString();
        authDAO.createAuth(token, username);

        authDAO.deleteAuth(token);
        assertNull(authDAO.getAuth(token));
    }

    @Test
    void deleteAuthNegative() throws DataAccessException {
        var missing = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> authDAO.deleteAuth(missing));
        assertNull(authDAO.getAuth(missing));
    }

    @Test
    void clearTest() throws AlreadyTakenException, DataAccessException {
        var username = uniqueUser();
        userDAO.createUser(username, "pw", uniqueEmail());
        authDAO.createAuth("token1", username);
        authDAO.createAuth("token2", username);

        authDAO.clear();

        assertNull(authDAO.getAuth("token1"));
        assertNull(authDAO.getAuth("token2"));
    }

    @Test
    void setupAuthTableTest() {
        assertDoesNotThrow(authDAO::setupAuthTable);
    }

    private static String uniqueUser() {
        return "auth_user_" + UUID.randomUUID();
    }

    private static String uniqueEmail() {
        return "auth+" + UUID.randomUUID() + "@example.com";
    }
}
