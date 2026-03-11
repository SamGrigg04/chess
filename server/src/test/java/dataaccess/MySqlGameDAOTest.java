package dataaccess;

import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlGameDAOTest {
    private static MySqlGameDAO gameDAO;
    private static MySqlAuthDAO authDAO;
    private static MySqlUserDAO userDAO;

    @BeforeAll
    static void setUpTables() throws DataAccessException {
        gameDAO = new MySqlGameDAO();
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();

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
    void createGamePositive() throws DataAccessException {
        var id = gameDAO.createGame(uniqueGameName());

        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    void createGameNegative() {
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void getGamePositive() throws DataAccessException {
        var name = uniqueGameName();
        var id = gameDAO.createGame(name);

        GameData data = gameDAO.getGame(id);
        assertNotNull(data);
        assertEquals(name, data.gameName());
    }

    @Test
    void getGameNegative() throws DataAccessException {
        assertNull(gameDAO.getGame(-1));
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        var first = gameDAO.createGame(uniqueGameName());
        var second = gameDAO.createGame(uniqueGameName());

        Collection<GameData> games = gameDAO.listGames();
        assertTrue(games.stream().anyMatch(g -> g.gameID().equals(first)));
        assertTrue(games.stream().anyMatch(g -> g.gameID().equals(second)));
    }

    @Test
    void listGamesNegative() throws DataAccessException {
        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    void updateGamePositive() throws DataAccessException {
        var id = gameDAO.createGame(uniqueGameName());

        gameDAO.updateGame(id, "WHITE", "white");
        gameDAO.updateGame(id, "BLACK", "black");

        GameData updated = gameDAO.getGame(id);
        assertEquals("white", updated.whiteUsername());
        assertEquals("black", updated.blackUsername());
    }

    @Test
    void updateGameNegative() throws DataAccessException {
        var id = gameDAO.createGame(uniqueGameName());

        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(id, "INVALID", "nobody"));
    }

    @Test
    void clearTest() throws DataAccessException {
        gameDAO.createGame(uniqueGameName());
        gameDAO.clear();

        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    void setupGameTableTest() {
        assertDoesNotThrow(gameDAO::setupGameTable);
    }

    private static String uniqueGameName() {
        return "game_" + UUID.randomUUID();
    }
}
