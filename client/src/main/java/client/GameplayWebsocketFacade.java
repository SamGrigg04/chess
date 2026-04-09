package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class GameplayWebsocketFacade {
    private final String serverUrl;
    private final ServerMessageObserver observer;
    private final Gson gson = new Gson();

    public GameplayWebsocketFacade(String serverUrl, ServerMessageObserver observer) {
        this.serverUrl = serverUrl;
        this.observer = observer;
    }

    public void connect(String authToken, int gameID) {

    }

    public void sendMove(MakeMoveCommand command) {

    }

    public void leaveGame(String authToken, int gameID) {

    }

    public void resign(String authToken, int gameID) {

    }

    public void disconnect() {

    }

    public String websocketUrl() {
        // http://host:port -> ws://host:port/ws
        return serverUrl.replaceFirst("^http", "ws") + "/ws";
    }

    // TODO: Something with messages
    public void handleMessage(String messageJson) {
        JsonObject payload = gson.fromJson(messageJson, JsonObject.class);
        ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.valueOf(
                payload.get("serverMessageType").getAsString()
        );

        switch (type) {
            case LOAD_GAME -> {
                LoadGameMessage message = gson.fromJson(messageJson, LoadGameMessage.class);
                observer.onLoadGame(message.getGame());
            }
            case ERROR -> {
                ErrorMessage message = gson.fromJson(messageJson, ErrorMessage.class);
                observer.onError(message.getErrorMessage());
            }
            case NOTIFICATION -> {
                NotificationMessage message = gson.fromJson(messageJson, NotificationMessage.class);
                observer.onNotification(message.getMessage());
            }
        }
    }

    public interface ServerMessageObserver {
        void onLoadGame(ChessGame game);

        void onNotification(String message);

        void onError(String errorMessage);
    }
}
