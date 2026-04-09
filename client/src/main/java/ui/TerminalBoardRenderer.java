package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.util.Collection;

import static ui.EscapeSequences.*;

public final class TerminalBoardRenderer {
    static final String EMPTY = "   ";
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    public void render(PrintStream out, BoardRenderModel model) {
        out.print(ERASE_SCREEN);
        drawHeaders(out, model.columnHeaders());
        drawChessBoard(out, model);
        drawHeaders(out, model.columnHeaders());
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void drawHeaders(PrintStream out, String[] headers) {
        setBlack(out);
        for (String header : headers) {
            drawHeader(out, header);
        }
        out.println();
    }

    private void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = 0;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private void printHeaderText(PrintStream out, String text) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(text);
        setBlack(out);
    }

    private void drawChessBoard(PrintStream out, BoardRenderModel model) {
        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES - 2; ++boardRow) {
            ChessPiece[] rowContents = new ChessPiece[BOARD_SIZE_IN_SQUARES - 2];
            for (int boardColumn = 1; boardColumn <= BOARD_SIZE_IN_SQUARES - 2; ++boardColumn) {
                rowContents[boardColumn - 1] = model.board().getPiece(new ChessPosition(boardRow, boardColumn));
            }
            String rowHeader = model.rowHeaders()[boardRow - 1];
            drawRowOfSquares(out, boardRow - 1, rowContents, rowHeader, model);
        }
    }

    private void drawRowOfSquares(PrintStream out,
                                  int boardRow,
                                  ChessPiece[] rowContents,
                                  String rowHeader,
                                  BoardRenderModel model) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            setLightGrey(out);
            drawHeader(out, rowHeader);

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES - 2; ++boardCol) {
                ChessPiece piece = rowContents[boardCol];
                ChessPosition currentPosition = new ChessPosition(boardRow + 1, boardCol + 1);
                boolean isLightSquare = (boardRow + boardCol) % 2 == 0;

                if (isLightSquare) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }

                if (model.startPosition() != null) {
                    highlightMoves(out, isLightSquare, currentPosition, model.startPosition(), model.endPositions());
                }

                out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS / 2));
                if (piece != null && piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    out.print(model.yourPieceTextColor());
                    out.print(pieceSymbol(piece));
                } else if (piece != null) {
                    out.print(model.opposingPieceTextColor());
                    out.print(pieceSymbol(piece));
                } else {
                    out.print(EMPTY);
                }
            }

            drawHeader(out, rowHeader);
            out.println();
        }
    }

    private void highlightMoves(PrintStream out,
                                boolean isLightSquare,
                                ChessPosition currentPosition,
                                ChessPosition startPosition,
                                Collection<ChessPosition> endPositions) {
        if (currentPosition.equals(startPosition)) {
            setHighlightYellow(out);
        } else if (endPositions != null && isLightSquare && endPositions.contains(currentPosition)) {
            setHighlightWhite(out);
        } else if (endPositions != null && endPositions.contains(currentPosition)) {
            setHighlightBlack(out);
        }
    }

    private void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setHighlightWhite(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setHighlightBlack(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setHighlightYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private String pieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> KING;
            case QUEEN -> QUEEN;
            case ROOK -> ROOK;
            case BISHOP -> BISHOP;
            case PAWN -> PAWN;
            case KNIGHT -> KNIGHT;
        };
    }
}
