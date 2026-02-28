package service;

import Request.CreateRequest;
import Request.JoinRequest;
import Result.AuthResult;
import Result.CreateResult;
import Result.ListResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.Objects;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public ListResult listGames(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (!Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        return new ListResult(gameDAO.listGames());
    }

    public CreateResult createGame(CreateRequest createRequest, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (!Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }

        String gameName = createRequest.gameName();
        Integer gameID = gameDAO.createGame(gameName);

        return new CreateResult(gameID);
    }

    public CreateResult joinGame(JoinRequest joinRequest, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        String playerColor = joinRequest.playerColor();
        Integer gameID = joinRequest.GameID();

        if (!Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new NoGameException("bad request");
        }
        if (Objects.equals(gameData.blackUsername(), playerColor)) {
            throw new AlreadyTakenException("already taken");
        }
        if (Objects.equals(gameData.whiteUsername(), playerColor)) {
            throw new AlreadyTakenException("already taken");
        }

        String username = authData.username();
        gameDAO.updateGame(gameID, playerColor, username);

        return new CreateResult(gameID);
    }

}
