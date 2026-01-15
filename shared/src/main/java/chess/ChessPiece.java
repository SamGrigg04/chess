package chess;

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
        throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition, PieceType type) {
        if (type == PieceType.KING) {
            /*
            a king can move one up, one down, one right, one left, or diagonally one.

            if possible move

            if the row/col == 0 or == 9, not a valid move
             */
            new ChessMove(myPosition, myPosition, null);
            throw new RuntimeException("Not implemented");

        } else if (type == PieceType.QUEEN) {

            throw new RuntimeException("Not implemented");

        } else if (type == PieceType.BISHOP) {

            throw new RuntimeException("Not implemented");

        } else if (type == PieceType.ROOK) {

            throw new RuntimeException("Not implemented");

        } else if (type == PieceType.KNIGHT) {

            throw new RuntimeException("Not implemented");

        } else if (type == PieceType.PAWN) {

            throw new RuntimeException("Not implemented");

        } else {
            throw new RuntimeException("Piece has no type");
        }



    }
}
