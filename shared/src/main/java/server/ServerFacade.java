//package server;
//
//import model.AuthData;
//import model.GameData;
//
//import java.util.List;
//
//public class ServerFacade {
//    private final String serverUrl;
//
//    public ServerFacade(int port) {
//        this.serverUrl = "http://localhost:" + port;
//    }
//
//    public AuthData register(String username, String password, String email) {
//        return null;
//    }
//
//    public AuthData login(String username, String password) {
//        return null;
//    }
//
//    public void logout(String authToken) {
//
//    }
//
//    public int createGame(String authToken, String gameName) {
//        return 0;
//    }
//
//    public List<GameData> listGames(String authToken) {
//        return null;
//    }
//
//    public void joinGame(String authToken, int gameID, String playerColor) {
//
//    }
//}

package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ChessGame createGame(ChessGame game) throws ResponseException {
        var request = buildRequest("POST", "/game", game);
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

//    public void deleteGame(int id) throws ResponseException {
//        var path = String.format("/pet/%s", id);
//        var request = buildRequest("DELETE", path, null);
//        var response = sendRequest(request);
//        handleResponse(response, null);
//    }

//    public void deleteAllGames() throws ResponseException {
//        var request = buildRequest("DELETE", "/pet", null);
//        sendRequest(request);
//    }

    public ArrayList<ChessGame> listGames() throws ResponseException {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
        return null;
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
