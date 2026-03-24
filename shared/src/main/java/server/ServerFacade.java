package server;

import com.google.gson.Gson;
import exception.ResponseException;

import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import java.util.List;

/*
talks to endpoints (login, logout, register, list, etc.)
translates json payloads to AuthData/GameData
throws ResponseExceptions (nicely) for any errors
 */

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public ServerFacade(String url) throws ResponseException {
        serverUrl = url;
    }

    public AuthData register(UserData user) throws ResponseException {
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(UserData user) throws ResponseException { //LoginRequest instead of userData?
        var request = buildRequest("POST", "/session", user);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException {
        var request = buildRequest("DELETE", "/session", authToken);
        var response = sendRequest(request);
        handleResponse(response, AuthData.class);
    }

    public List<GameData> listGames(String authToken) throws ResponseException {
        return null;
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        return 0;
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws ResponseException {

    }

    public void clear() throws ResponseException {

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
            return client.send(request, HttpResponse.BodyHandlers.ofString());
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
