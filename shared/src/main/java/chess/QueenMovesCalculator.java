package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(slide(this::upMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::downMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::leftMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::rightMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::upRightMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::upLeftMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::downRightMove, board, myPosition, false));
        possibleMoves.addAll(slide(this::downLeftMove, board, myPosition, false));
        return possibleMoves;
    }
}
