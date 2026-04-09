package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.tyrus.client.ClientManager;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import jakarta.websocket.*;

import java.net.URI;

public class GameplayWebsocketFacade extends Endpoint {
    private final String serverUrl;
    private final ServerMessageObserver observer;
    private final Gson gson = new Gson();
    private Session session;

    public GameplayWebsocketFacade(String serverUrl, ServerMessageObserver observer) {
        this.serverUrl = serverUrl;
        this.observer = observer;
    }

    public void connect(String authToken, int gameID) {
        try {
            ClientManager client = ClientManager.createClient(); // Creates a new client
            session = client.connectToServer(this, URI.create(websocketUrl())); // connects to /ws

            sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID)); // sends a CONNECT command
        } catch (Exception e) {
            observer.onError("Failed to connect to game: " + e.getMessage());
        }
    }

    public void sendMove(MakeMoveCommand command) {
        sendCommand(command);
    }

    public void leaveGame(String authToken, int gameID) {
        sendCommand(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
    }

    public void resign(String authToken, int gameID) {
        sendCommand(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
    }

    public void disconnect() {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (Exception e) {
            observer.onError("Error disconnecting: " + e.getMessage());
        }
    }

    private void sendCommand(Object command) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(gson.toJson(command));
            }
        } catch (Exception e) {
            observer.onError("Failed to send command: " + e.getMessage());
        }
    }

    public String websocketUrl() {
        // http://host:port -> ws://host:port/ws
        return serverUrl.replaceFirst("^http", "ws") + "/ws";
    }

    // when a connection opens, handle the messages
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        try {
            session.addMessageHandler(String.class, this::handleMessage);
        } catch (Exception e) {
            observer.onError("Failed to set up message handler: " + e.getMessage());
        }
    }

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
