package ui;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;

public record BoardRenderModel(
        String[] columnHeaders,
        String[] rowHeaders,
        ChessBoard board,
        ChessPosition startPosition,
        Collection<ChessPosition> endPositions,
        String yourPieceTextColor,
        String opposingPieceTextColor,
        BoardPerspective perspective
) {
}
