package websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.result.ConnectResult;
import websocket.result.LeaveResult;
import websocket.result.MoveResult;
import websocket.result.ResignResult;

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

    public MoveResult makeMove(UserGameCommand command, Session session) {
        return null;
    }

    public LeaveResult leave(UserGameCommand command, Session session) {
        return null;
    }

    public ResignResult resign(UserGameCommand command, Session session) {
        return null;
    }
}
