package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(upSlide(board, myPosition));
        possibleMoves.addAll(downSlide(board, myPosition));
        possibleMoves.addAll(rightSlide(board, myPosition));
        possibleMoves.addAll(leftSlide(board, myPosition));


        return possibleMoves;
    }
}
