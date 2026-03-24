package server;

import model.AuthData;
import model.GameData;

import java.util.List;

/*
talks to endpoints (login, logout, register, list, etc.)
translates json payloads to AuthData/GameData
throws ResponseExceptions (nicely) for any errors
 */

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

    public List<GameData> listGames(String authToken) {
        return null;
    }

    public int createGame(String authToken, String gameName) {
        return 0;
    }

    public void joinGame(String authToken, int gameID, String playerColor) {

    }

    public void clear() {

    }
}
