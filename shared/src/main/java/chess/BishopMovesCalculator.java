package chess;

import java.util.Collection;

public class BishopMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves){
        possibleMoves.addAll(slide(this::upRightMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::upLeftMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::downRightMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::downLeftMove, board, myPosition, false));
        return possibleMoves;
    }
}
