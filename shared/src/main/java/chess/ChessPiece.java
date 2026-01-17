package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    final ChessGame.TeamColor pieceColor;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * pieceMoves: Given a board configuration, this method returns all the moves a specific piece can
     * legally make independant of whose turn it is or if the King is being attacked.
     * It considers the edges of the board and the location of both enemy and friendly pieces.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

/*
        Start with the king. all he can do is move one space in any direction unless a piece on his team is in
        that position. If a piece is captured, just rewrite the space to contain the King.

        Don't reuse code, that's bad practice. Write some methods that move directions and use those for multiple pieces

        DON'T MAKE SUBCLASSES, make a move calculator class with kingmove/queenmove subclasses. Then depending
        on the pieceType, you know which thing to make a new instance of
 */
        /*
        get the piece at the position
        call the moveCalculator class
        return the array
         */
        return moveCalculator(board, myPosition, null);
    }

//    holdup this can totally just be the up down left right diagonal thing, but what about knights?
    private Collection<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition, PieceType type) {
        // Need to figure out what to do with bishops, rooks, and the queen who can move more than one.
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        if (type == PieceType.BISHOP || type == PieceType.KING || type == PieceType.QUEEN || type == PieceType.ROOK) {
            if (type != PieceType.ROOK) {
                //Diagonal
                if (myPosition.row != 8 && myPosition.col != 8) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col + 1);
                    if ("no friendlies") {
                        ChessMove rightUpMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(rightUpMove);
                    }
                }
                if (myPosition.row != 1 && myPosition.col != 8) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col + 1);
                    if ("no friendlies") {
                        ChessMove leftUpMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(leftUpMove);
                    }
                }
                if (myPosition.row != 8 && myPosition.col != 1) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col - 1);
                    if ("no friendlies") {
                        ChessMove rightDownMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(rightDownMove);
                    }
                }
                if (myPosition.row != 1 && myPosition.col != 1) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col - 1);
                    if ("no friendlies") {
                        ChessMove leftDownMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(leftDownMove);
                    }
                }
            }

            // Left, right, up, down so far
            if (type != PieceType.BISHOP) {
                if (myPosition.row != 1) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col);
                    if ("there isn't a friendly piece at that position") {
                        ChessMove leftMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(leftMove);
                    }
                }

                if (myPosition.row != 8) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col);
                    ChessMove rightMove = new ChessMove(myPosition, endPosition, null);
                    possibleMoves.add(rightMove);
                }

                if (myPosition.col != 8) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row, myPosition.col + 1);
                    ChessMove upMove = new ChessMove(myPosition, endPosition, null);
                    possibleMoves.add(upMove);
                }

                if (myPosition.col != 1) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row, myPosition.col - 1);
                    ChessMove downMove = new ChessMove(myPosition, endPosition, null);
                    possibleMoves.add(downMove);
                }
            }
        }

        if (type == PieceType.PAWN) {
            // It can move up two maybe
            throw new RuntimeException("Not implemented");
        }

        if (type == PieceType.KNIGHT) {
            // Does funny knight stuff
            throw new RuntimeException("Not implemented");
        }

//        check diagonals (up + left) (up + right) (down + left) (down + right) (King, Queen, Bishop)

//        check for knight (up 2 + right or left, down 2 + right or left, left 2 + up or down, right 2 + up or down)

        else {
            throw new RuntimeException("Piece has no type");
        }

    return possibleMoves;

    }
}
