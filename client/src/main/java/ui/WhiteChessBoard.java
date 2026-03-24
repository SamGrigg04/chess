package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class WhiteChessBoard {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    private static final String EMPTY = "   ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {
        setBlack(out);

        String[] headers = { EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = 0;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {
        String[][] initBoard = {
                {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "},
                {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {" p ", " p ", " p ", " p ", " p ", " p ", " p ", " p "},
                {" r ", " n ", " b ", " q ", " k ", " b ", " n ", " r "}
        };
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES - 2; ++boardRow) {
            String[] rowContents = initBoard[boardRow];
            drawRowOfSquares(out, boardRow, rowContents);
        }

    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, String[] rowContents) {

        String[] headers = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            setLightGrey(out);

            drawHeader(out, headers[boardRow]);

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES - 2; ++boardCol) {

                if (boardRow % 2 == 0) {
                    if (boardCol % 2 == 0) {
                        setWhite(out);
                    } else {
                        setBlack(out);
                    }
                } else {
                    if (boardCol % 2 == 0) {
                        setBlack(out);
                    } else {
                        setWhite(out);
                    }
                }

                int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                int suffixLength = 0;

                out.print(EMPTY.repeat(prefixLength));
                if (boardRow <= 1) {
                    out.print(SET_TEXT_COLOR_BLUE);
                }
                if (boardRow >= 6) {
                    out.print(SET_TEXT_COLOR_RED);
                }
                out.print(rowContents[boardCol]);
                out.print(EMPTY.repeat(suffixLength));

            }

            drawHeader(out, headers[boardRow]);

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
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
