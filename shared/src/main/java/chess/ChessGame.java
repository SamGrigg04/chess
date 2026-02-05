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
        throw new RuntimeException("Not implemented");
//        if the collection is empty, call isInCheckmate.
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
        if (validMoves(move.startPosition).isEmpty() || getTeamTurn() != getBoard().getPiece(move.startPosition).pieceColor) {
            throw new InvalidMoveException();
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
        throw new RuntimeException("Not implemented");
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
        if (getTeamTurn() == teamColor) {
            Collection<ChessMove> allValidMoves = new ArrayList<>();
            checkAllTeamMoves(teamColor, allValidMoves);
            return (allValidMoves.isEmpty() && isInCheck(teamColor));
        }
        return false;
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
            Collection<ChessMove> allValidMoves = new ArrayList<>();
            checkAllTeamMoves(teamColor, allValidMoves); //TODO: do i need to do allValidMoves = ___? If so, change function type from void
            return (allValidMoves.isEmpty() && !isInCheck(teamColor));
        }
        return false;
    }


    private void checkAllTeamMoves(TeamColor teamColor, Collection<ChessMove> allValidMoves) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if (currentPiece != null && currentPiece.pieceColor == teamColor) {
                    allValidMoves.addAll(validMoves(new ChessPosition(row, col)));
                }
            }
        }
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
