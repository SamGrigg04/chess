package chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor team = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();

    public ChessGame() {
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     *
     * *2*
     * Take all of your starting moves, and after each move call inCheck. If it returns true,
     * you're good. If not, throw away the move.
     *
     * Iterate through all your valid moves cloning the board each time.
     * If they work there, return them. *Need to make ChessBoard clonable*
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) { return null; }

        Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();


        for (ChessMove move : pieceMoves) {
            ChessBoard clonedBoard = board.clone();
            ChessPiece currentPiece = clonedBoard.getPiece(startPosition);
            executeMove(clonedBoard, move, currentPiece);
            if (!isInCheck(currentPiece.pieceColor)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     *
     * invalid if not a validMove or not your turn
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece currentPiece = getBoard().getPiece(move.startPosition);
        Collection<ChessMove> validMoves = validMoves(move.startPosition);
        if (validMoves == null
                || !validMoves.contains(move)
                || getTeamTurn() != currentPiece.pieceColor) {
            throw new InvalidMoveException();
        }

        executeMove(getBoard(), move, currentPiece);

        TeamColor enemyColor;
        if (currentPiece.pieceColor == TeamColor.BLACK) {
            enemyColor = TeamColor.WHITE;
        } else if (currentPiece.pieceColor == TeamColor.WHITE) {
            enemyColor = TeamColor.BLACK;
        } else {
            throw new RuntimeException("no team color???");
        }
        setTeamTurn(enemyColor);
    }

    private void executeMove(ChessBoard board, ChessMove move, ChessPiece currentPiece) {
        if (move.promotionPiece != null) {
            board.addPiece(move.endPosition, new ChessPiece(currentPiece.pieceColor, move.promotionPiece));
            board.addPiece(move.startPosition, null);
        } else {
            board.addPiece(move.endPosition, currentPiece);
            board.addPiece(move.startPosition, null);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     *
     * *1*
     * check all opponent moves. if the endPosition on any of the moves
     * ends where your king is, you're in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyColor;
        if (teamColor == TeamColor.BLACK) {
            enemyColor = TeamColor.WHITE;
        } else if (teamColor == TeamColor.WHITE) {
            enemyColor = TeamColor.BLACK;
        } else {
            throw new RuntimeException("no team color???");
        }

        ChessPosition kingPosition = getKingPosition(teamColor);

        Collection<ChessMove> allEnemyMoves = checkAllTeamMoves(enemyColor, new ArrayList<>());
        for (ChessMove move : allEnemyMoves) {
            if (move.endPosition.equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInCheckOnBoard(TeamColor teamColor, ChessBoard board) {
        TeamColor enemyColor;
        if (teamColor == TeamColor.BLACK) {
            enemyColor = TeamColor.WHITE;
        } else if (teamColor == TeamColor.WHITE) {
            enemyColor = TeamColor.BLACK;
        } else {
            throw new RuntimeException("no team color???");
        }

        ChessPosition kingPosition = getKingPosition(board, teamColor);

        Collection<ChessMove> allEnemyMoves = checkAllTeamMoves(enemyColor);
        for (ChessMove move : allEnemyMoves) {
            if (move.endPosition.equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     *
     * call validMoves. if empty, checkmate (and your turn) (and in check already)
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheckmateOnBoard(teamColor, this.board);
    }

    private boolean isInCheckmateOnBoard(TeamColor teamColor, ChessBoard board) {
        if (getTeamTurn() != teamColor || !isInCheck(teamColor)) { return false; }

        for (ChessMove move : checkAllTeamMoves(teamColor)) {
            ChessBoard clone = board.clone();
            ChessPiece piece = clone.getPiece(move.startPosition);
            executeMove(clone, move, piece);

            if (!isInCheck(teamColor)) {
                return false;
            }
        }
        return true;
    }

    private ChessPosition getKingPosition(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {
            if (kingPosition != null) { break; }
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if (currentPiece != null && currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.pieceColor == teamColor) {
                    kingPosition = new ChessPosition(row, col);
                    break;
                }
            }
        }
        return kingPosition;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     *
     * call validMoves. if empty and not in check, true
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (getTeamTurn() == teamColor) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);

                    if (piece != null && piece.pieceColor == teamColor) {
                        Collection<ChessMove> moves = validMoves(pos);
                        if (moves != null && !moves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }


    private Collection<ChessMove> checkAllTeamMoves(TeamColor teamColor, Collection<ChessMove> allValidMoves) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if (currentPiece != null && currentPiece.pieceColor == teamColor) {
                    allValidMoves.addAll(currentPiece.pieceMoves(getBoard(), new ChessPosition(row, col)));
                }
            }
        }
        return allValidMoves;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, board);
    }

}
