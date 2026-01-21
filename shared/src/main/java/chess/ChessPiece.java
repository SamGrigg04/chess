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
     * <p>
     * pieceMoves: Given a board configuration, this method returns all the moves a specific piece can
     * legally make independant of whose turn it is or if the King is being attacked.
     * It considers the edges of the board and the location of both enemy and friendly pieces.
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == ChessPiece.PieceType.KING) {
            return new KingMovesCalculator().pieceMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.QUEEN) {
            return new QueenMovesCalculator().pieceMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.BISHOP) {
            return new BishopMovesCalculator().pieceMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.KNIGHT) {
            return new KnightMovesCalculator().pieceMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.ROOK) {
            return new RookMovesCalculator().pieceMoves(board, myPosition);
        } else if (type == ChessPiece.PieceType.PAWN) {
            return new PawnMovesCalculator().pieceMoves(board, myPosition);
        } else {
            throw new RuntimeException("No piece type [ChessPiece.java]");
        }
    }
}
