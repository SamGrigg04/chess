package client;

public class GameplayWebsocketFacade {

    public GameplayWebsocketFacade() {

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



    public interface ServerMessageObserver {
        void onLoadGame(ChessGame game);

        void onNotification(String message);

        void onError(String errorMessage);
    }
}
