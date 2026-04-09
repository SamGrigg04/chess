package client;

import chess.ChessGame;

public interface ServerMessageObserver {
    void onLoadGame(ChessGame game);

    void onNotification(String message);

    void onError(String errorMessage);
}
