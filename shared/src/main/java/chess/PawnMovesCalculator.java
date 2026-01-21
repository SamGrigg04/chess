package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
//        Collection<ChessMove> possibleMoves = new ArrayList<>();
//        // TODO: Logic in MoveCalculator?
        // TODO: pawns can capture diagonally
        // TODO: for each promotion possibility, make a new move
//        if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
//            possibleMoves.addAll(upMove(board, myPosition));
//            possibleMoves.addAll(upTwoMove(board, myPosition));
//            possibleMoves.addAll(promotionMove(board, myPosition));
//        } else if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) {
//            possibleMoves.addAll(downMove(board, myPosition));
//        }

        return null;
//        return possibleMoves;
    }
}
