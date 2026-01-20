package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(upSlide(board, myPosition));
        possibleMoves.addAll(downSlide(board, myPosition));
        possibleMoves.addAll(rightSlide(board, myPosition));
        possibleMoves.addAll(leftSlide(board, myPosition));
        possibleMoves.addAll(upRightSlide(board, myPosition));
        possibleMoves.addAll(upLeftSlide(board, myPosition));
        possibleMoves.addAll(downRightSlide(board, myPosition));
        possibleMoves.addAll(downLeftSlide(board, myPosition));
        return possibleMoves;
    }
}
