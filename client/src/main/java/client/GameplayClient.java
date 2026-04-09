package client;

import chess.ChessGame;
import serverFacade.ServerFacade;

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
    };

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

    public String highlightMoves() {
        return null;
    }

    public String redrawBoard() {
        return null;
    }

    public String leaveGame() {
        return null;
    }

    public String makeMove() {
        return null;
    }

    public String resign() {
        return null;
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

}
