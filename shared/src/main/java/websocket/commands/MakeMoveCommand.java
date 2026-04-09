package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
    }

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        this(CommandType.MAKE_MOVE, authToken, gameID, move);
    }

    public ChessMove getMove() {
        return move;
    }
}
