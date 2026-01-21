package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
//        possibleMoves.addAll(slide(this::upRightMove, board, myPosition));
//        possibleMoves.addAll(slide(this::downRightMove, board, myPosition));
//        possibleMoves.addAll(slide(this::upLeftMove, board, myPosition));
//        possibleMoves.addAll(slide(this::downLeftMove, board, myPosition));
        return possibleMoves;
    }
}
