package websocket.result;

import chess.ChessGame;

public record ConnectResult(ChessGame game, String notificationText) {
}
