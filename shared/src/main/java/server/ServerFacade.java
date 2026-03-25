package server;

import com.google.gson.Gson;
import exception.ResponseException;

import model.GameData;
import request.*;
import result.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import java.util.*;

/*
talks to endpoints (login, logout, register, list, etc.)
translates json payloads to AuthData/GameData
throws ResponseExceptions (nicely) for any errors
 */

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient(); // creates requests and gets back responses
    private final String serverUrl; // includes host name and port number


    public ServerFacade(String url) throws ResponseException {
        serverUrl = url;
    }

    public AuthResult register(String username, String password, String email) throws ResponseException {
        var request = buildRequest("POST", "/user", new RegisterRequest(username, password, email), null);
        var response = sendRequest(request);
        return handleResponse(response, AuthResult.class);
    }

    public AuthResult login(String username, String password) throws ResponseException { //LoginRequest instead of userData?
        var request = buildRequest("POST", "/session", new LoginRequest(username, password), null);
        var response = sendRequest(request);
        return handleResponse(response, AuthResult.class);
    }

    public void logout(String authToken) throws ResponseException {
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public Collection<GameData> listGames(String authToken) throws ResponseException {
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);

        var listResult = handleResponse(response, ListResult.class);
        if (listResult == null || listResult.games() == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(listResult.games());
    }

    public int createGame(String gameName, String authToken) throws ResponseException {
        var request = buildRequest("POST", "/game", new CreateRequest(gameName), authToken);
        var response = sendRequest(request);
        var createResult = handleResponse(response, CreateResult.class);

        if (createResult == null) { // non-initialized int has a value of 0
            return 0;
        }

        return createResult.gameID();
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws ResponseException {
        var request = buildRequest("PUT", "/game", new JoinRequest(playerColor, gameID), authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("Authorization", authToken);
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
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            throw toResponseException(response.body(), status);
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
