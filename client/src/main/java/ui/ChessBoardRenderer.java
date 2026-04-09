package ui;

import chess.ChessBoard;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public final class ChessBoardRenderer {
    private static final TerminalBoardRenderer TERMINAL_RENDERER = new TerminalBoardRenderer();

    private ChessBoardRenderer() {
    }

    public static void render(ChessBoard board,
                              BoardPerspective perspective,
                              ChessPosition startPosition,
                              Collection<ChessPosition> endPositions) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        TERMINAL_RENDERER.render(out, perspective.model(board, startPosition, endPositions));
    }
}
