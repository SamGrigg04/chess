package chess;

import java.util.Collection;

public class RookMovesCalculator extends MoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves){
        // Returns all possible moves the rook can make
        possibleMoves.addAll(slide(this::upMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::downMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::leftMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::rightMove, board, myPosition, false));
        return possibleMoves;
    }
}
