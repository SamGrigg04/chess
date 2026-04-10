package ui;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public enum BoardPerspective {
    BLACK(
            new String[]{TerminalBoardRenderer.EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", TerminalBoardRenderer.EMPTY},
            new String[]{" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "}
    ),
    WHITE(
            new String[]{TerminalBoardRenderer.EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", TerminalBoardRenderer.EMPTY},
            new String[]{" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "}
    );

    private final String[] columnHeaders;
    private final String[] rowHeaders;

    BoardPerspective(String[] columnHeaders, String[] rowHeaders) {
        this.columnHeaders = columnHeaders;
        this.rowHeaders = rowHeaders;
    }

    public BoardRenderModel model(ChessBoard board,
                                  ChessPosition startPosition,
                                  Collection<ChessPosition> endPositions) {
        ChessBoard displayBoard = this == BLACK ? reverseBoard(board) : board;
        return new BoardRenderModel(
                columnHeaders,
                rowHeaders,
                displayBoard,
                displayPosition(startPosition),
                displayPositions(endPositions),
                SET_TEXT_COLOR_RED,
                SET_TEXT_COLOR_BLUE,
                this
        );
    }

    public ChessPosition displayPosition(ChessPosition position) {
        if (position == null) {
            return null;
        }
        return this == BLACK
                ? new ChessPosition(position.getRow(), 9 - position.getColumn())
                : position;
    }

    public Collection<ChessPosition> displayPositions(Collection<ChessPosition> positions) {
        Collection<ChessPosition> displayPositions = new ArrayList<>();
        if (positions == null) {
            return displayPositions;
        }
        for (ChessPosition position : positions) {
            displayPositions.add(displayPosition(position));
        }
        return displayPositions;
    }

    private ChessBoard reverseBoard(ChessBoard board) {
        ChessBoard reversedBoard = new ChessBoard();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                reversedBoard.addPiece(new ChessPosition(9 - row, 9 - col), board.getPiece(new ChessPosition(row, col)));
            }
        }
        return reversedBoard;
    }
}
