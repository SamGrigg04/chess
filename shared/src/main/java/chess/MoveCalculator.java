package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves) {return new ArrayList<>();}

    @FunctionalInterface
    interface Direction {
        Collection<ChessPosition> position(ChessPosition myPosition);
    }

    protected Collection<ChessPosition> upMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8) {
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> downMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1) {
            possibleMoves.add(new ChessPosition(myPosition.row - 1, myPosition.col));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> rightMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.col < 8) {
            possibleMoves.add(new ChessPosition(myPosition.row, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> leftMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.col > 1) {
            possibleMoves.add(new ChessPosition(myPosition.row, myPosition.col - 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> upRightMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col < 8) {
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> upLeftMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col > 1) {
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col - 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> downRightMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col < 8) {
            possibleMoves.add(new ChessPosition(myPosition.row - 1, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> downLeftMove(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col > 1) {
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
                    return promotionMove(myPosition, endPosition);
                }
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
                    return promotionMove(myPosition, endPosition);
                }
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
                    return promotionMove(myPosition, endPosition);
                }
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
                    return promotionMove(myPosition, endPosition);
                }
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
                    return promotionMove(myPosition, endPosition);
                }
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
                    return promotionMove(myPosition, endPosition);
                }
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> promotionMove(ChessPosition myPosition, ChessPosition endPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight1(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 7 && myPosition.col < 8) {
            possibleMoves.add(new ChessPosition(myPosition.row + 2, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight2(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col < 7) {
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col + 2));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight3(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col < 7) {
            possibleMoves.add(new ChessPosition(myPosition.row - 1, myPosition.col + 2));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight4(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 2 && myPosition.col < 8) {
            possibleMoves.add(new ChessPosition(myPosition.row - 2, myPosition.col + 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight5(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 2 && myPosition.col > 1) {
            possibleMoves.add(new ChessPosition(myPosition.row - 2, myPosition.col - 1));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight6(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row > 1 && myPosition.col > 2) {
            possibleMoves.add(new ChessPosition(myPosition.row - 1, myPosition.col - 2));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight7(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 8 && myPosition.col > 2) {
            possibleMoves.add(new ChessPosition(myPosition.row + 1, myPosition.col -2));
        }
        return possibleMoves;
    }

    protected Collection<ChessPosition> knight8(ChessPosition myPosition) {
        Collection<ChessPosition> possibleMoves = new ArrayList<>();
        if (myPosition.row < 7 && myPosition.col > 1) {
            possibleMoves.add(new ChessPosition(myPosition.row + 2, myPosition.col - 1));
        }
        return possibleMoves;
    }


    // Pass in a function (upMove, downMove, etc.), the board, and the position of the piece to be moved.
    // The single boolean is for the king, pawns, and knights.
    protected Collection<ChessMove> slide (Direction dir, ChessBoard board, ChessPosition myPosition, boolean single) {
        // The ArrayList of ChessMoves we are going to return
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        // Initialize the variable that's going to be iterated
        ChessPosition newEndPosition = myPosition;
        // While there is a valid next move (trying to move does not return an empty container)
        while (!dir.position(newEndPosition).isEmpty()) {
            // Make a valid move in the passed in direction (we know it will work because of the while loop condition)
            ArrayList<ChessPosition> nextPosCollection = (ArrayList<ChessPosition>)dir.position(newEndPosition);
            ChessPosition potentialNextPos = nextPosCollection.getFirst();
            // Various checks for valid moves
            if (board.getPiece(potentialNextPos) != null) {
                // If we capture, make a move and end there
                if (board.getPiece(myPosition).pieceColor != board.getPiece(potentialNextPos).pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, potentialNextPos, null));
                    break;
                // If there is a friendly piece in the way, stop
                } else if (board.getPiece(myPosition).pieceColor == board.getPiece(potentialNextPos).pieceColor) {
                    break;
                // For sanity purposes
                } else {
                    throw new RuntimeException("Something wonky in here");
                }
            }
            // Look at the next square in the sequence (this is the variable we iterate
            // so that the piece keeps moving
            newEndPosition = potentialNextPos;
            // Add the move to the collection
            possibleMoves.add(new ChessMove(myPosition, newEndPosition, null));
            // If the piece does not slide, break the loop
            if (single) {break;}
        }
        // Return the collection of possible moves in the given direction
        return possibleMoves;
    }
}

