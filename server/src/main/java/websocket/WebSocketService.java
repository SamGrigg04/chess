package websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.result.ConnectResult;

public class WebSocketService {
    private final GameDAO gameDAO;

    public WebSocketService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public ConnectResult connect(UserGameCommand command) throws ResponseException, DataAccessException {
        if (!validateAuth(command.getAuthToken())) {
            throw new ResponseException("Error: unauthorized");
        }
        if (!validateGame(command.getGameID())) {
            throw new ResponseException("Error: bad request");
        }

        ChessGame game = gameDAO.getGame(command.getGameID()).game();

        String notificationText = ""; //TODO

        return new ConnectResult(game, notificationText);
    }

    public boolean validateAuth(String AuthToken) {
        return false;
    }

    public boolean validateGame(Integer gameID) {
        return false;
    }
}
