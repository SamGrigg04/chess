package client;

import model.AuthData;
import model.GameData;

import java.util.List;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) {
        return null;
    }

    public AuthData login(String username, String password) {
        return null;
    }

    public void logout(String authToken) {

    }

    public int createGame(String authToken, String gameName) {
        return 0;
    }

    public List<GameData> listGames(String authToken) {
        return null;
    }

    public void joinGame(String authToken, int gameID, String playerColor) {

    }
}
