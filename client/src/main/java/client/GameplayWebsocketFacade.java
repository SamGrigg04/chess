package client;

import chess.ChessGame;

public class GameplayWebsocketFacade {
    private final String serverUrl;
    private final ServerMessageObserver observer;

    public GameplayWebsocketFacade(String serverUrl, ServerMessageObserver observer) {
        this.serverUrl = serverUrl;
        this.observer = observer;
    }

    public void connect() {

    }

    public void sendMove() {

    }

    public void leaveGame() {

    }

    public void resign() {

    }

    public void disconnect() {

    }

    public String websocketUrl() {
        return serverUrl;
    }

    public interface ServerMessageObserver {
        void onLoadGame(ChessGame game);

        void onNotification(String message);

        void onError(String errorMessage);
    }
}
