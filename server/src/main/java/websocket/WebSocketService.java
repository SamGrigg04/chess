package websocket;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.result.ConnectResult;
import websocket.result.LeaveResult;
import websocket.result.MoveResult;
import websocket.result.ResignResult;

import java.util.Objects;

public class WebSocketService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ConnectResult connect(UserGameCommand command) throws ResponseException, DataAccessException {
        if (!validateAuth(command.getAuthToken())) {
            throw new ResponseException("Error: unauthorized");
        }
        if (!validateGame(command.getGameID())) {
            throw new ResponseException("Error: bad request");
        }

        String username = authDAO.getAuth(command.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);

        String notificationText = String.format("%s joined the game as %s",
                username,
                playerColor == null ? "an observer" : playerColor.toString());

        return new ConnectResult(game, notificationText);
    }

    public MoveResult makeMove(UserGameCommand command, Session session) throws ResponseException, DataAccessException {
        if (!validateAuth(command.getAuthToken())) {
            throw new ResponseException("Error: unauthorized");
        }
        if (!validateGame(command.getGameID())) {
            throw new ResponseException("Error: bad request");
        }
        if (!validateMove()) {
            throw new ResponseException("Error: invalid move");
        }

        String username = authDAO.getAuth(command.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);

        if (playerColor == null) {
            throw new ResponseException("Error: unauthorized");
        }
        if (game.getTeamTurn() != playerColor) {
            throw new ResponseException("Error: not your turn");
        }

        String statusNotificationText = "";

        String moveNotificationText = String.format("%s moved from <STARTPOSITION> to <ENDPOSITION>",
                username);

        return new MoveResult(game, moveNotificationText, statusNotificationText);
    }

    public boolean validateMove() {
        return false;
    }

    public LeaveResult leave(UserGameCommand command, Session session) {

        String notificationText = "<PLAYERNAME> left the game";
        return null;
    }

    public ResignResult resign(UserGameCommand command, Session session) {

        String notificationText = "<PLAYERNAME> resigned";
        return null;
    }

    public boolean validateAuth(String AuthToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(AuthToken);
        return authData != null;
    }

    public boolean validateGame(Integer gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        return gameData != null;
    }

    public ChessGame.TeamColor getPlayerColor(GameData gameData, String username) {
        ChessGame.TeamColor playerColor;
        if (Objects.equals(gameData.blackUsername(), username)) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else if (Objects.equals(gameData.whiteUsername(), username)) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else {
            playerColor = null;
        }
        return playerColor;
    }
}
