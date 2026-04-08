package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public final class ChessBoardRenderer {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String EMPTY = "   ";

    // calls the thing that prints the board (passes in the config)
    public static void render(ChessBoard board,
                              PlayerColor playerColor,
                              ChessPosition startPosition,
                              Collection<ChessPosition> endPositions) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        render(out,
                playerColor.config(board),
                playerColor.displayPosition(startPosition),
                playerColor.displayPositions(endPositions));
    }

    // prints the chess board to the terminal based on player color passed in
    private static void render(PrintStream out,
                               ChessBoardConfig config,
                               ChessPosition startPosition,
                               Collection<ChessPosition> endPositions) {
        // clears the screen (supposedly)
        out.print(ERASE_SCREEN);

        // prints the headers at the top, then the board spaces (with the side headers), then the bottom headers
        drawHeaders(out, config.columnHeaders());
        drawChessBoard(out, config, startPosition, endPositions);
        drawHeaders(out, config.columnHeaders());

        // Sets colors generally for the whole UI
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    // prints a header for each element in the headers string
    private static void drawHeaders(PrintStream out, String[] headers) {
        setBlack(out);

        for (String header : headers) {
            drawHeader(out, header);
        }

        out.println();
    }

    // Draws the header
    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = 0; // This will need to change if I want to use the special characters (see pet shop)

        // Keeping the scaffolding for the special characters for now even though it doesn't do anything
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    // Finally actually prints the thing to the terminal. abstraction is weird
    private static void printHeaderText(PrintStream out, String text) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(text);

        setBlack(out);
    }

    // draws the chess board row by row
    private static void drawChessBoard(PrintStream out,
                                       ChessBoardConfig config,
                                       ChessPosition startPosition,
                                       Collection<ChessPosition> endPositions) {
        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES - 2; ++boardRow) {
            ChessPiece[] rowContents = new ChessPiece[BOARD_SIZE_IN_SQUARES - 2];
            for (int boardColumn = 1; boardColumn <= BOARD_SIZE_IN_SQUARES - 2; ++boardColumn) {
                // gets the data for the row to be printed
                rowContents[boardColumn - 1] = config.board().getPiece(new ChessPosition(boardRow, boardColumn));
            }
            // gets the data for the header (row number) to put on either side
            String rowHeader = config.rowHeaders()[boardRow - 1];
            drawRowOfSquares(out, boardRow - 1, rowContents, rowHeader, config, startPosition, endPositions);
        }
    }

    private static void drawRowOfSquares(PrintStream out,
                                         int boardRow,
                                         ChessPiece[] rowContents,
                                         String rowHeader,
                                         ChessBoardConfig config,
                                         ChessPosition startPosition,
                                         Collection<ChessPosition> endPositions) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            // puts the row number in first
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

                if (startPosition != null && endPositions != null) {
                    highlightMoves(out, isLightSquare, currentPosition, startPosition, endPositions);
                }

                int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                int suffixLength = 0;

                String yourPieceTextColor = config.yourPieceTextColor();
                String opposingPieceTextColor = config.opposingPieceTextColor();

                out.print(EMPTY.repeat(prefixLength));
                if (piece != null && piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    out.print(yourPieceTextColor);
                    out.print(pieceSymbol(piece));
                } else if (piece != null) {
                    out.print(opposingPieceTextColor);
                    out.print(pieceSymbol(piece));
                } else {
                    out.print(EMPTY);
                }
                out.print(EMPTY.repeat(suffixLength));
            }

            // prints the row number at the end
            drawHeader(out, rowHeader);
            out.println();
        }
    }

    private static void highlightMoves(PrintStream out,
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

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        // this is only for the headers, so I always want the color to be black
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setHighlightWhite(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setHighlightBlack(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setHighlightYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    // Today I learned you can put a little record in a class. Very nice for config stuff
    public record ChessBoardConfig(
            String[] columnHeaders, // column letters
            String[] rowHeaders, // row numbers
            ChessBoard board, // board
            String yourPieceTextColor, // opposing player color
            String opposingPieceTextColor // your player color
    ) { /* and I need nothing in here because it is a record */ }

    public enum PlayerColor {
        BLACK(
                new ChessBoardConfig(
                        new String[]{EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", EMPTY},
                        new String[]{" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "},
                        null,
                        null,
                        null
                )
        ),
        WHITE(
                new ChessBoardConfig(
                        new String[]{EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY},
                        new String[]{" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "},
                        null,
                        null,
                        null
                )
        );

        private final ChessBoardConfig config;

        PlayerColor(ChessBoardConfig config) {
            this.config = config;
        }

        ChessBoardConfig config(ChessBoard board) {
            ChessBoard displayBoard = this == BLACK ? reverseBoard(board) : board;
            return new ChessBoardConfig(
                    config.columnHeaders(),
                    config.rowHeaders(),
                    displayBoard,
                    SET_TEXT_COLOR_BLUE,
                    SET_TEXT_COLOR_RED
            );
        }

        ChessPosition displayPosition(ChessPosition position) {
            if (position == null) {
                return null;
            }
            return this == BLACK
                    ? new ChessPosition(position.getRow(), 9 - position.getColumn())
                    : new ChessPosition(9 - position.getRow(), position.getColumn());
        }

        Collection<ChessPosition> displayPositions(Collection<ChessPosition> positions) {
            Collection<ChessPosition> displayPositions = new ArrayList<>();
            if (positions == null) {
                return displayPositions;
            }
            for (ChessPosition position : positions) {
                displayPositions.add(displayPosition(position));
            }
            return displayPositions;
        }
    }

    private static ChessBoard reverseBoard(ChessBoard board) {
        ChessBoard reversedBoard = new ChessBoard();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                reversedBoard.addPiece(new ChessPosition(9 - row, 9 - col), board.getPiece(new ChessPosition(row, col)));
            }
        }
        return reversedBoard;
    }

    private static String pieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> KING;
            case QUEEN -> QUEEN;
            case ROOK -> ROOK;
            case BISHOP -> BISHOP;
            case PAWN -> PAWN;
            case KNIGHT -> KNIGHT;

            /*
            If I want to use fancy characters
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
            */
        };
    }

}
