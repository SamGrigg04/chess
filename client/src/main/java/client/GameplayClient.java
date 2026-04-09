package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import serverFacade.ServerFacade;
import ui.BoardPerspective;
import ui.ChessBoardRenderer;

import java.util.ArrayList;
import java.util.Collection;

public class GameplayClient implements ServerMessageObserver{
    private final ServerFacade server;
    private final ClientSession session;

    public GameplayClient(/*String serverURL,*/ ServerFacade server, ClientSession session) {
        this.server = server;
        this.session = session;
    }

    public String joinGame(String... params) throws ResponseException {
        session.assertSignedIn();
        if (params.length < 2) {
            throw new ResponseException("Expected gameID and player color");
        }

        int gameID = parseGameID(params[0]);
        BoardPerspective perspective = parsePerspective(params[1]);

        server.joinGame(perspective.name(), gameID, session.getAuthToken());

        GameData game = findGame(gameID);
        session.startGame(gameID, perspective, false, game);
        renderCurrentBoard(null, null);

        // TODO: call the join endpoint, open a websocket connection, send a CONNECT message, transition to gameplay UI
        return String.format("Joined game with id %s", gameID);
    }

    public String observeGame(String... params) throws ResponseException {
        session.assertSignedIn();
        if (params.length < 1) {
            throw new ResponseException("Expected gameID");
        }

        int gameID = parseGameID(params[0]);
        GameData game = findGame(gameID);
        session.startGame(gameID, BoardPerspective.WHITE, true, game);
        renderCurrentBoard(null, null);

        // TODO: do not call the join endpoint, open a websocket connection, send a CONNECT message, transition to gameplay UI
        return String.format("Observing game with id %s", gameID);
    }

    public String highlightMoves(String... params) throws ResponseException {
        session.assertPlaying();
        if (params.length < 1) {
            throw new ResponseException("Expected a position");
        }

        ChessPosition startPosition = parsePosition(params[0]);
        GameData game = requireCurrentGame();
        ChessBoard board = game.game().getBoard();
        if (board.getPiece(startPosition) == null) {
            throw new ResponseException("No piece at that position");
        }

        Collection<ChessPosition> endPositions = new ArrayList<>();
        Collection<ChessMove> validMoves = game.game().validMoves(startPosition);
        if (validMoves == null || validMoves.isEmpty()) {
            throw new ResponseException("No valid moves found");
        }
        for (ChessMove move : validMoves) {
            endPositions.add(move.getEndPosition());
        }

        renderCurrentBoard(startPosition, endPositions);
        return String.format("Highlighted possible moves for position %s", params[0]);
    }

    public String redrawBoard() throws ResponseException {
        session.assertPlaying();
        renderCurrentBoard(null, null);
        return "Board redrawn";
    }

    public String leaveGame() throws ResponseException {
        session.assertPlaying();

        // TODO: Disconnect websocket connection
        session.leaveGame();
        return "Left game";
    }

    public String makeMove(String... params) throws ResponseException {
        session.assertPlaying();
        if (session.isObserver()) {
            throw new ResponseException("Observers cannot make moves");
        }
        if (params.length < 2) {
            throw new ResponseException("Expected start and end positions");
        }

        ChessMove move = parseMove(params);
        // TODO: Send over websocket
        return String.format("Sent move request %s to %s", params[0], params[1]);
    }

    // TODO: Does not cause the player to leave the game
    public String resign(String... params) throws ResponseException {
        session.assertPlaying();
        if (session.isObserver()) {
            throw new ResponseException("Observers cannot resign");
        }
        if (params.length < 1 || !"YES".equalsIgnoreCase(params[0].trim())) {
            return "Resignation cancelled";
        }

        // TODO: end the game for all participands
        return "Resignation request sent";
    }

    @Override
    public void onLoadGame(ChessGame game) {

    }

    @Override
    public void onNotification(String message) {

    }

    @Override
    public void onError(String errorMessage) {

    }

    private void renderCurrentBoard(ChessPosition startPosition, Collection<ChessPosition> endPositions) throws ResponseException {
        GameData game = requireCurrentGame();
        ChessBoardRenderer.render(game.game().getBoard(), session.getActivePerspective(), startPosition, endPositions);
    }

    private GameData requireCurrentGame() throws ResponseException {
        GameData currentGame = session.getCurrentGame();
        if (currentGame == null) {
            throw new ResponseException("No game loaded");
        }
        return currentGame;
    }

    private GameData findGame(int gameID) throws ResponseException {
        for (GameData game : server.listGames(session.getAuthToken())) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new ResponseException("Game not found");
    }

    private int parseGameID(String input) throws ResponseException {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException ex) {
            throw new ResponseException("Game ID must be a number");
        }
    }

    private BoardPerspective parsePerspective(String input) throws ResponseException {
        try {
            return BoardPerspective.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseException("Player color must be white or black");
        }
    }

    private ChessPosition parsePosition(String input) throws ResponseException {
        String position = input.trim().toLowerCase();
        if (position.length() != 2) {
            throw new ResponseException("Invalid position");
        }

        char file = position.charAt(0);
        char rank = position.charAt(1);
        if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
            throw new ResponseException("Invalid position");
        }
        return new ChessPosition(rank - '0', file - 'a' + 1);
    }

    private ChessMove parseMove(String... params) throws ResponseException {
        ChessPosition start = parsePosition(params[0]);
        ChessPosition end = parsePosition(params[1]);
        ChessPiece.PieceType promotion = null;

        if (params.length >= 3 && !params[2].isBlank()) {
            promotion = parsePromotionPiece(params[2]);
        }
        return new ChessMove(start, end, promotion);
    }

    private ChessPiece.PieceType parsePromotionPiece(String input) throws ResponseException {
        return switch (input.trim().toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new ResponseException("Promotion piece must be queen, rook, bishop, or knight");
        };
    }

}
