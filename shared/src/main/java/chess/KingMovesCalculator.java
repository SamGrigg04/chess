package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
//        possibleMoves.addAll(upMove(board, myPosition));
//        possibleMoves.addAll(downMove(board, myPosition));
//        possibleMoves.addAll(rightMove(board, myPosition));
//        possibleMoves.addAll(leftMove(board, myPosition));
//        possibleMoves.addAll(upRightMove(board, myPosition));
//        possibleMoves.addAll(upLeftMove(board, myPosition));
//        possibleMoves.addAll(downRightMove(board, myPosition));
//        possibleMoves.addAll(downLeftMove(board, myPosition));
        return possibleMoves;
    }
}
