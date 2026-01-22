package chess;

import java.util.Collection;

public class KnightMovesCalculator extends MoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves){
        // Tries all 8 possible moves by a knight going clockwise starting at the 1:00 position
        possibleMoves.addAll(slide(this::knight1, board, myPosition, true));
        possibleMoves.addAll(slide(this::knight2, board, myPosition, true));
        possibleMoves.addAll(slide(this::knight3, board, myPosition, true));
        possibleMoves.addAll(slide(this::knight4, board, myPosition, true));
        possibleMoves.addAll(slide(this::knight5, board, myPosition, true));
        possibleMoves.addAll(slide(this::knight6, board, myPosition, true));
        possibleMoves.addAll(slide(this::knight7, board, myPosition, true));
        possibleMoves.addAll(slide(this::knight8, board, myPosition, true));
        return possibleMoves;
    }
}
