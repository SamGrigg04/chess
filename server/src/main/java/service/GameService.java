package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import result.CreateResult;
import result.ListResult;

import java.util.Objects;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListResult listGames(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null || !Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        return new ListResult(gameDAO.listGames());
    }

    public CreateResult createGame(CreateRequest createRequest, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null || !Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }

        String gameName = createRequest.gameName();
        Integer gameID = gameDAO.createGame(gameName);
        return new CreateResult(gameID);
    }

    public void joinGame(JoinRequest joinRequest, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null || !Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }

        String playerColor = joinRequest.playerColor();
        Integer gameID = joinRequest.gameID();

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null || playerColor == null ||
                (!Objects.equals(playerColor, "BLACK") && !Objects.equals(playerColor, "WHITE"))) {
            throw new NoGameException("bad request");
        }

        if (Objects.equals(playerColor, "WHITE") && gameData.whiteUsername() != null) {
            throw new AlreadyTakenException("already taken");
        }
        if (Objects.equals(playerColor, "BLACK") && gameData.blackUsername() != null) {
            throw new AlreadyTakenException("already taken");
        }

        gameDAO.updateGame(gameID, playerColor, authData.username());
    }
}
