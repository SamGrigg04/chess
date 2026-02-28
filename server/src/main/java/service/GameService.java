package service;

import Request.CreateRequest;
import Request.JoinRequest;
import Result.CreateResult;
import Result.ListResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;

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

    public void joinGame(JoinRequest joinRequest) {}

}
