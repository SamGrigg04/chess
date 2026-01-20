package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("No piece type [ChessPiece.java]");

//        return new ArrayList<ChessMove>();
    }

    protected Collection<ChessMove> upMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.col != 8) {
            System.out.println("up");
            ChessPosition endPosition = new ChessPosition(myPosition.row, myPosition.col + 1);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> downMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.col != 1) {
            System.out.println("down");
            ChessPosition endPosition = new ChessPosition(myPosition.row, myPosition.col - 1);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> rightMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row != 8) {
            System.out.println("right");
            ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> leftMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row != 1) {
            System.out.println("left");
            ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> upRightMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row != 8 && myPosition.col != 8) {
            System.out.println("right up");
            ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col + 1);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> upLeftMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row != 1 && myPosition.col != 8) {
            System.out.println("left up");
            ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col + 1);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> downRightMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row != 8 && myPosition.col != 1) {
            System.out.println("right down");
            ChessPosition endPosition = new ChessPosition(myPosition.row + 1, myPosition.col - 1);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> downLeftMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (myPosition.row != 1 && myPosition.col != 1) {
            System.out.println("left down");
            ChessPosition endPosition = new ChessPosition(myPosition.row - 1, myPosition.col - 1);
            if (board.getPiece(endPosition) == null || board.getPiece(myPosition).pieceColor != board.getPiece(endPosition).pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }





/*
            public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // TODO: instead of all the nested ifs, have the parent moveCalculator with children KingMovesCalculator, QueenMoves
        // TODO: calculator, etc. Parent will have shared code

        // TODO: Need to figure out what to do with bishops, rooks, and the queen who can move more than one.
        // TODO: Make a loop inside those subclasses that terminates when it hits a piece of the same color (when a
        // TODO: move is not added to the collection? Maybe a toggle?)

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
