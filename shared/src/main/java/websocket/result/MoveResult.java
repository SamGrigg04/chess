package websocket.result;

import chess.ChessGame;

public record MoveResult(ChessGame game, String moveNotification, String statusNotification) {
}
