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


        return moveCalculator(board, myPosition, board.getPiece(myPosition).getPieceType());
    }

//    holdup this can totally just be the up down left right diagonal thing, but what about knights?
    private Collection<ChessMove> moveCalculator(ChessBoard board, ChessPosition myPosition, PieceType type) {
        // TODO: instead of all the nested ifs, have the parent moveCalculator with children KingMovesCalculator, QueenMoves
        // TODO: calculator, etc. Parent will have shared code

        // TODO: Need to figure out what to do with bishops, rooks, and the queen who can move more than one.
        // TODO: Make a loop inside those subclasses that terminates when it hits a piece of the same color (when a
        // TODO: move is not added to the collection? Maybe a toggle?)



        Collection<ChessMove> possibleMoves = new ArrayList<>();
        System.out.printf("type: %s row: %d col: %d %n", type, myPosition.row, myPosition.col);

        if (type == PieceType.BISHOP || type == PieceType.KING || type == PieceType.QUEEN || type == PieceType.ROOK) {
            if (type != PieceType.ROOK) {
                //Diagonal
                if (myPosition.row != 8 && myPosition.col != 8) {
                    System.out.println("right up");
                    ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col + 1);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove rightUpMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(rightUpMove);
                    }
                }
                if (myPosition.row != 1 && myPosition.col != 8) {
                    System.out.println("left up");
                    ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col + 1);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove leftUpMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(leftUpMove);
                    }
                }
                if (myPosition.row != 8 && myPosition.col != 1) {
                    System.out.println("right down");
                    ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col - 1);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove rightDownMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(rightDownMove);
                    }
                }
                if (myPosition.row != 1 && myPosition.col != 1) {
                    System.out.println("left down");
                    ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col - 1);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove leftDownMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(leftDownMove);
                    }
                }
            }

            // Left, right, up, down so far
            if (type != PieceType.BISHOP) {
                if (myPosition.row != 1) {
                    System.out.println("left");
                    ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove leftMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(leftMove);
                    }
                }

                if (myPosition.row != 8) {
                    System.out.println("right");
                    ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove rightMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(rightMove);
                    }
                }

                if (myPosition.col != 8) {
                    System.out.println("up");
                    ChessPosition endPosition = new ChessPosition(myPosition.row, myPosition.col + 1);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove upMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(upMove);
                    }
                }

                if (myPosition.col != 1) {
                    System.out.println("down");
                    ChessPosition endPosition = new ChessPosition(myPosition.row, myPosition.col - 1);
                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        ChessMove downMove = new ChessMove(myPosition, endPosition, null);
                        possibleMoves.add(downMove);
                    }
                }
            }
        }

        if (type == PieceType.KNIGHT) {
            // Does funny knight stuff

        }

        if (type == PieceType.PAWN) {
            // It can move up two maybe
            if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
                if (myPosition.row != 8) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col);

                    if (myPosition.row == 2) {
                        endPosition = new ChessPosition(myPosition.row + 2, myPosition.col);
                        if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                            ChessMove upTwo = new ChessMove(myPosition, endPosition, null);
                            possibleMoves.add(upTwo);
                        }
                    }

                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        if (endPosition.row == 8) {
                            ChessMove upPromote = new ChessMove(myPosition, endPosition, type);
                            possibleMoves.add(upPromote);
                        }
                    }
                    ChessMove upMove = new ChessMove(myPosition, endPosition, null);
                    possibleMoves.add(upMove);
                }

            } else if (myPosition.row != 1) {
                    ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col);

                    if (myPosition.row == 7) {
                        endPosition = new ChessPosition(myPosition.row - 2, myPosition.col);
                        if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                            ChessMove downTwo = new ChessMove(myPosition, endPosition, null);
                            possibleMoves.add(downTwo);
                        }
                    }

                    if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                        if (endPosition.row == 1) {
                            ChessMove downPromote = new ChessMove(myPosition, endPosition, type);
                            possibleMoves.add(downPromote);
                        }
                    }
                    ChessMove downMove = new ChessMove(myPosition, endPosition, null);
                    possibleMoves.add(downMove);
            }
        }


//        check for knight (up 2 + right or left, down 2 + right or left, left 2 + up or down, right 2 + up or down)


        return possibleMoves;

    }

    private Collection<ChessMove> kingMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> queenMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> bishopMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> knightMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> rookMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> pawnMovesCalculator(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {
        throw new RuntimeException("Not implemented");
    }

}
