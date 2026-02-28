package service;

import Request.CreateRequest;
import Request.JoinRequest;
import Result.CreateResult;
import Result.ListResult;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public ListResult listGames() {
        return null;
    }

    public void joinGame(JoinRequest joinRequest) {}

    public CreateResult createGame(CreateRequest createRequest) {
        return null;
    }
}
