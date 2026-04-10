package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import websocket.commands.MakeMoveCommand;
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
        validate(command);

        String username = authDAO.getAuth(command.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);

        String notificationText = String.format("%s joined the game as %s",
                username,
                playerColor == null ? "an observer" : playerColor.toString());

        return new ConnectResult(game, notificationText);
    }

    public MoveResult makeMove(UserGameCommand command) throws ResponseException, DataAccessException {
        validate(command);

        String username = authDAO.getAuth(command.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);

        if (playerColor == null) {
            throw new ResponseException("Error: observers can't make moves");
        }
        if (game.getTeamTurn() != playerColor) {
            throw new ResponseException("Error: not your turn");
        }
        if (game.isGameOver()) {
            throw new ResponseException("Error: the game is over");
        }

        MakeMoveCommand moveCommand = (MakeMoveCommand) command;
        ChessMove move = moveCommand.getMove();

        try {
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            throw new ResponseException("Error: invalid move");
        }

        ChessGame.TeamColor opponentColor = playerColor == ChessGame.TeamColor.WHITE
                ? ChessGame.TeamColor.BLACK
                : ChessGame.TeamColor.WHITE;

        String opponentUsername = opponentColor == ChessGame.TeamColor.WHITE
                ? gameData.whiteUsername()
                : gameData.blackUsername();

        String statusNotificationText = "";
        if (game.isInCheckmate(opponentColor)) {
            statusNotificationText = opponentUsername + " is in checkmate";
            game.setIsGameOver(true);
        } else if (game.isInCheck(opponentColor)) {
            statusNotificationText = opponentUsername + " is in check";
        } else if (game.isInStalemate(opponentColor)) {
            statusNotificationText = opponentUsername + " is in stalemate";
            game.setIsGameOver(true);
        }

        gameDAO.saveGame(command.getGameID(), game);

        String moveNotificationText = String.format("%s moved from %s to %s",
                username,
                positionToString(move.getStartPosition()),
                positionToString(move.getEndPosition()));

        return new MoveResult(game, moveNotificationText, statusNotificationText);
    }

    private String positionToString(ChessPosition pos) {
        return (char)('a' + (pos.getColumn() - 1)) + String.valueOf(pos.getRow());
    }

    public LeaveResult leave(UserGameCommand command) throws DataAccessException, ResponseException {
        validate(command);

        String username = authDAO.getAuth(command.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);

        if (playerColor != null) {
            gameDAO.updateGame(command.getGameID(), playerColor.toString(), null);
        }
        gameDAO.saveGame(command.getGameID(), gameData.game());

        String notificationText = String.format("%s left the game", username);
        return new LeaveResult(notificationText);
    }

    public ResignResult resign(UserGameCommand command) throws ResponseException, DataAccessException {
        validate(command);

        String username = authDAO.getAuth(command.getAuthToken()).username();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        ChessGame.TeamColor playerColor = getPlayerColor(gameData, username);

        if (game.isGameOver()) {
            throw new ResponseException("Error: game is over");
        }
        if (playerColor == null) {
            throw new ResponseException("Error: unauthorized");
        }

        gameData.game().setIsGameOver(true);
        gameDAO.saveGame(command.getGameID(), game);

        String notificationText = String.format("%s resigned", username);
        return new ResignResult(notificationText);
    }

    private void validate(UserGameCommand command) throws ResponseException, DataAccessException {
        if (!validateAuth(command.getAuthToken())) {
            throw new ResponseException("Error: unauthorized");
        }
        if (!validateGame(command.getGameID())) {
            throw new ResponseException("Error: bad request");
        }
    }

    private boolean validateAuth(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        return authData != null;
    }

    private boolean validateGame(Integer gameID) throws DataAccessException {
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
