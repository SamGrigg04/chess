package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import result.AuthResult;
import server.Server;
import server.ServerFacade;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        String baseUrl = "http://localhost:" + port;
        facade = new ServerFacade(baseUrl);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clearDatabase() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void registerSuccess() throws ResponseException {
        AuthResult auth = facade.register("wowwhatausername", "toomanysecrets0000", "a@email.com");
        assertNotNull(auth.authToken());
        assertEquals("wowwhatausername", auth.username());
    }

    @Test
    void registerFail() throws ResponseException {
        facade.register("twin", "a", "f@email.com");
        assertThrows(ResponseException.class,
                () -> facade.register("twin", "a", "f@email.com"));
    }

    @Test
    void loginSuccess() throws ResponseException {
        facade.register("k", "d", "wheeeeee");
        AuthResult auth = facade.login("k", "d");
        assertNotNull(auth.authToken());
    }

    @Test
    void loginFail() {
        assertThrows(ResponseException.class, () -> facade.login("nope", "wrong"));
    }

    @Test
    void logoutSuccess() throws ResponseException {
        AuthResult auth = facade.register("m", "n", "02@email.org");
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    void logoutFail() {
        assertThrows(ResponseException.class, () -> facade.logout("bogus"));
    }

    @Test
    void createGameSuccess() throws ResponseException {
        AuthResult maker = facade.register("nerd", "supernerd", "bah@k.co");
        int gameId = facade.createGame("help i need a life outside of chess", maker.authToken());
        assertTrue(gameId > 0);
    }

    @Test
    void createGameFail() throws ResponseException {
        AuthResult maker = facade.register("whats", "in", "a@name.com");
        assertThrows(ResponseException.class,
                () -> facade.createGame("", maker.authToken()));
    }

    @Test
    void listGamesSuccess() throws ResponseException {
        AuthResult maker = facade.register("listy", "losty", "lusty@t.com");
        int id = facade.createGame("Test(2)(3).txt", maker.authToken());
        Collection<?> games = facade.listGames(maker.authToken());
        assertFalse(games.isEmpty());
    }

    @Test
    void listGamesFail() {
        assertThrows(ResponseException.class, () -> facade.listGames("wrong"));
    }

    @Test
    void joinGameSuccess() throws ResponseException {
        AuthResult host = facade.register("bigboi", "large", "incharge@mail.com");
        int id = facade.createGame("BigGame", host.authToken());
        AuthResult guest = facade.register("smallboi", "smol", "teeny@mail.com");
        assertDoesNotThrow(() -> facade.joinGame("WHITE", id, guest.authToken()));
    }

    @Test
    void joinGameFail() throws ResponseException {
        AuthResult host = facade.register("largefry", "potato", "mc@d.com");
        int id = facade.createGame("Game", host.authToken());
        AuthResult guest = facade.register("smallcoke", "n03", "con@glomerate.coke");
        assertThrows(ResponseException.class,
                () -> facade.joinGame("PURPLE", id, guest.authToken()));
    }

    @Test
    void clearTest() throws ResponseException {

    }

}