package ui;

import chess.ChessBoard;
import chess.ChessMove;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public final class ChessBoardRenderer {
    ChessBoard board;
    Collection<ChessMove> moves;

    public ChessBoardRenderer(ChessBoard board, Collection<ChessMove> moves) {
        this.board = board;
        this.moves = moves;
    }

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String EMPTY = "   ";

    // calls the thing that prints the board (passes in the config)
    public static void render(PlayerColor playerColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        render(out, playerColor.config());
    }

    // prints the chess board to the terminal based on player color passed in
    private static void render(PrintStream out, ChessBoardConfig config) {
        // clears the screen (supposedly)
        out.print(ERASE_SCREEN);

        // prints the headers at the top, then the board spaces (with the side headers), then the bottom headers
        drawHeaders(out, config.columnHeaders());
        drawChessBoard(out, config);
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
    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setBlack(out);
    }

    // draws the chess board row by row
    private static void drawChessBoard(PrintStream out, ChessBoardConfig config) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES - 2; ++boardRow) {
            // gets the data for the row to be printed
            String[] rowContents = config.initialBoard()[boardRow];
            // gets the data for the header (row number) to put on either side
            String rowHeader = config.rowHeaders()[boardRow];
            drawRowOfSquares(out, boardRow, rowContents, rowHeader, config.topPieceTextColor(), config.bottomPieceTextColor());
        }
    }

    // draws each row
    private static void drawRowOfSquares(PrintStream out,
                                         int boardRow,
                                         String[] rowContents,
                                         String rowHeader,
                                         String topPieceTextColor,
                                         String bottomPieceTextColor) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            // puts the row number in first
            setLightGrey(out);

            drawHeader(out, rowHeader);

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES - 2; ++boardCol) {
                // fancy logic I'm very proud of to determine what color the square should be
                boolean useWhiteSquare = (boardRow % 2 == 0) == (boardCol % 2 == 0);

                if (useWhiteSquare) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }

                int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                int suffixLength = 0;

                out.print(EMPTY.repeat(prefixLength));
                // sets the colors depending on who is viewing the board
                if (boardRow <= 1) {
                    out.print(topPieceTextColor);
                }
                if (boardRow >= 6) {
                    out.print(bottomPieceTextColor);
                }
                // prints the contents of the row
                out.print(rowContents[boardCol]);
                out.print(EMPTY.repeat(suffixLength));
            }

            // prints the row number at the end
            drawHeader(out, rowHeader);

            out.println();
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

    // Today I learned you can put a little record in a class. Very nice for config stuff
    public record ChessBoardConfig(
            String[] columnHeaders, // column letters
            String[] rowHeaders, // row numbers
            String[][] initialBoard, // initial setup
            String topPieceTextColor, // opposing player color
            String bottomPieceTextColor // your player color
    ) {
        // and I need nothing in here because it is a record
    }

    // This is where all the non-duplicate code goes
    public enum PlayerColor {
        BLACK(
                new ChessBoardConfig(
                        new String[]{EMPTY, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", EMPTY},
                        new String[]{" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "},
                        new String[][]{
                                {" R ", " N ", " B ", " K ", " Q ", " B ", " N ", " R "},
                                {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {" p ", " p ", " p ", " p ", " p ", " p ", " p ", " p "},
                                {" r ", " n ", " b ", " k ", " q ", " b ", " n ", " r "}
                        },
                        SET_TEXT_COLOR_RED, // top color
                        SET_TEXT_COLOR_BLUE // bottom color
                )
        ),
        WHITE(
                new ChessBoardConfig(
                        new String[]{EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY},
                        new String[]{" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "},
                        new String[][]{
                                {" r ", " n ", " b ", " q ", " k ", " b ", " n ", " r "},
                                {" p ", " p ", " p ", " p ", " p ", " p ", " p ", " p "},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
                                {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "}
                        },
                        SET_TEXT_COLOR_BLUE,
                        SET_TEXT_COLOR_RED
                )
        );

        private final ChessBoardConfig config;

        PlayerColor(ChessBoardConfig config) {
            this.config = config;
        }

        ChessBoardConfig config() {
            return config;
        }
    }

}
