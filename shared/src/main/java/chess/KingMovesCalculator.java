package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(slide(this::upMove, board, myPosition, true));
        possibleMoves.addAll(slide(this::downMove, board, myPosition, true));
        possibleMoves.addAll(slide(this::leftMove, board, myPosition, true));
        possibleMoves.addAll(slide(this::rightMove, board, myPosition, true));
        possibleMoves.addAll(slide(this::upRightMove, board, myPosition, true));
        possibleMoves.addAll(slide(this::upLeftMove, board, myPosition, true));
        possibleMoves.addAll(slide(this::downRightMove, board, myPosition, true));
        possibleMoves.addAll(slide(this::downLeftMove, board, myPosition, true));
        return possibleMoves;
    }
}
