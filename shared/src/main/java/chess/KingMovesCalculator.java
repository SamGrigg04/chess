package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(upMove(board, myPosition));
        possibleMoves.addAll(downMove(board, myPosition));
        possibleMoves.addAll(downMove(board, myPosition));
        possibleMoves.addAll(rightMove(board, myPosition));
        possibleMoves.addAll(leftMove(board, myPosition));


        return possibleMoves;
    }
}
