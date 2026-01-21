package chess;

import java.util.Collection;

public class PawnMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves){
        if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
            possibleMoves.addAll(upMovePawn(board, myPosition));
            possibleMoves.addAll(upTwoMove(board, myPosition));
            possibleMoves.addAll(upLeftPawn(board, myPosition));
            possibleMoves.addAll(upRightPawn(board, myPosition));
        }
        else if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) {
            possibleMoves.addAll(downMovePawn(board, myPosition));
            possibleMoves.addAll(downTwoMove(board, myPosition));
            possibleMoves.addAll(downLeftPawn(board, myPosition));
            possibleMoves.addAll(downRightPawn(board, myPosition));
        }
        return possibleMoves;
    }
}
