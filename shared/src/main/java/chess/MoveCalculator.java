package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {return new ArrayList<>();}

    @FunctionalInterface
    interface Direction {
        Collection<ChessPosition> position(ChessPosition myPosition);
    }

    protected Collection<ChessPosition> upMove(ChessPosition startPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (startPosition.row < 8) {
            System.out.println("up");
            possibleMoves.add(new ChessPosition(startPosition.row + 1, startPosition.col));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> downMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1) {
            System.out.println("down");
            possibleMoves.add(new ChessPosition(myPosition.row - 1, myPosition.col));

        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> rightMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.col < 8) {
            System.out.println("right");
            possibleMoves.add(new ChessPosition(myPosition.row, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> leftMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.col > 1) {
            System.out.println("left");
            possibleMoves.add(new ChessPosition(myPosition.row, myPosition.col - 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> upRightMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col < 8) {
            System.out.println("right up");
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> upLeftMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col > 1) {
            System.out.println("left up");
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col - 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> downRightMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col < 8) {
            System.out.println("right down");
            possibleMoves.add(new ChessPosition(myPosition.row - 1, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> downLeftMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col > 1) {
            System.out.println("left down");
            possibleMoves.add(new ChessPosition(myPosition.row - 1, myPosition.col - 1));
        }
        return possibleMoves;
    }

    // Pass in a function (upMove, downMove, etc.), the board, and the position of the piece to be moved.
    protected Collection<ChessMove> slide (Direction dir, ChessBoard board, ChessPosition myPosition) {

        // The ArrayList of ChessMoves we are going to return
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        // Initialize the variable that's going to be iterated
        ChessPosition newEndPosition = myPosition;
        System.out.printf("starting position: %s \n", myPosition);

        // While there is a valid next move (trying to move does not return an empty container)
        while (!dir.position(newEndPosition).isEmpty()) {

            // Make a valid move in the passed in direction (we know it will work because of the while loop condition)
            ArrayList<ChessPosition> nextPosCollection = (ArrayList<ChessPosition>)dir.position(newEndPosition);

            ChessPosition potentialNextPos = nextPosCollection.getFirst();
            if (!checkValid(myPosition, potentialNextPos, board)) {break;}

            possibleMoves.add(new ChessMove(myPosition, newEndPosition, null));
            newEndPosition = potentialNextPos;

//            currentMove.getFirst().startPosition = myPosition;
//            // Add the valid move to the list of valid moves
//            possibleMoves.addAll(currentMove);
//
//            System.out.printf("position after current move: %s \n", newStartPosition);
//            // Get the end position of the move we just made and store it as the new start position
//            newStartPosition = currentMove.getFirst().endPosition;
//
//            System.out.printf("position for next move: %s \n", newStartPosition);

        }
        return possibleMoves;
    }

    private boolean checkValid (ChessPosition startPosition, ChessPosition endPosition, ChessBoard board) {
        return board.getPiece(endPosition) == null || board.getPiece(startPosition).pieceColor != board.getPiece(endPosition).pieceColor;
    }

    // TODO: ALL start positions remain the same, end positions change.

    // TODO: take check logic out of upper funcs, make them just return a position.
//    TODO: pass that position into a boolean check method
//    TODO: if true, add that move to the array



/*
            public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        System.out.printf("type: %s row: %d col: %d %n", type, myPosition.row, myPosition.col);

        if (type == ChessPiece.PieceType.PAWN) {
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
        possibleMoves.add(upMove);
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


 */
}
