package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {return new ArrayList<>();}

    @FunctionalInterface
    interface Direction {
        Collection<ChessPosition> position(ChessPosition myPosition);
    }

    protected Collection<ChessPosition> upMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8) {
            System.out.println("up");
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col));
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

    protected Collection<ChessMove> upRightPawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col < 8) {
            ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col + 1);
            if (board.getPiece(endPosition) != null && board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                if (endPosition.row == 8) {
                    return promotionMove(board, myPosition, endPosition);
                }
                System.out.println("right up pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> upLeftPawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col > 1) {
            ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col - 1);
            if (board.getPiece(endPosition) != null && board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                if (endPosition.row == 8) {
                    return promotionMove(board, myPosition, endPosition);
                }
                System.out.println("left up pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> downRightPawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col < 8) {
            ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col + 1);
            if (board.getPiece(endPosition) != null && board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                if (endPosition.row == 1) {
                    return promotionMove(board, myPosition, endPosition);
                }
                System.out.println("right down pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> downLeftPawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col > 1) {
            ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col - 1);
            if (board.getPiece(endPosition) != null && board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                if (endPosition.row == 1) {
                    return promotionMove(board, myPosition, endPosition);
                }
                System.out.println("left down pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> upTwoMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row == 2) {
            ChessPosition firstStep = new ChessPosition(myPosition.row + 1, myPosition.col);
            ChessPosition endPosition = new ChessPosition(myPosition.row + 2, myPosition.col);
            if (board.getPiece(firstStep) == null && board.getPiece(endPosition) == null) {
                System.out.println("up two pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }


    protected Collection<ChessMove> downTwoMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row == 7) {
            ChessPosition firstStep = new ChessPosition(myPosition.row - 1, myPosition.col);
            ChessPosition endPosition = new ChessPosition(myPosition.row - 2, myPosition.col);
            if (board.getPiece(firstStep) == null && board.getPiece(endPosition) == null) {
                System.out.println("down two pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> upMovePawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8) {
            ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col);
            if (board.getPiece(endPosition) == null) {
                if (endPosition.row == 8) {
                    return promotionMove(board, myPosition, endPosition);
                }
                System.out.println("up pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> downMovePawn(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1) {
            ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col);
            if (board.getPiece(endPosition) == null) {
                if (endPosition.row == 1) {
                    return promotionMove(board, myPosition, endPosition);
                }
                System.out.println("down pawn");
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> promotionMove(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
        return possibleMoves;
    }


    // Pass in a function (upMove, downMove, etc.), the board, and the position of the piece to be moved.
    // The single boolean is for the king, pawns, and knights.
    protected Collection<ChessMove> slide (Direction dir, ChessBoard board, ChessPosition myPosition, boolean single) {

        // The ArrayList of ChessMoves we are going to return
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        // Initialize the variable that's going to be iterated
        ChessPosition newEndPosition = myPosition;
//        System.out.printf("starting position: %s \n", myPosition);

        // While there is a valid next move (trying to move does not return an empty container)
        while (!dir.position(newEndPosition).isEmpty()) {

            // Make a valid move in the passed in direction (we know it will work because of the while loop condition)
            ArrayList<ChessPosition> nextPosCollection = (ArrayList<ChessPosition>)dir.position(newEndPosition);

            ChessPosition potentialNextPos = nextPosCollection.getFirst();
            if (board.getPiece(potentialNextPos) != null) {
                if (board.getPiece(myPosition).pieceColor != board.getPiece(potentialNextPos).pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, potentialNextPos, null));
                    break;
                } else if (board.getPiece(myPosition).pieceColor == board.getPiece(potentialNextPos).pieceColor) {
                    break;
                } else {
                    throw new RuntimeException("Something wonky in here");
                }
            }

            newEndPosition = potentialNextPos;

            possibleMoves.add(new ChessMove(myPosition, newEndPosition, null));
            if (single) {break;}
        }
            System.out.printf("%s \n", possibleMoves);
            return possibleMoves;
    }
}

// TODO: KNIGHTS AND PAWNS MOVES