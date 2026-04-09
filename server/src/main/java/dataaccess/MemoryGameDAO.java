package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public Integer createGame(String gameName) {
        int gameID = generateID();
        while (games.containsKey(gameID)) {
            gameID = generateID();
        }

        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, gameData);
        return gameID;
    }

    @Override
    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void saveGame(Integer gameID, ChessGame game) throws DataAccessException {
        GameData currentGame = getGame(gameID);
        if (currentGame == null) {
            throw new DataAccessException("Game not found");
        }
        currentGame = new GameData(gameID, currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName(), game);
        games.put(gameID, currentGame);
    }

    @Override
    public void updateGame(Integer gameID, String playerColor, String username) {
        GameData currentGame = getGame(gameID);
        if (currentGame == null) {
            return;
        }
        if (Objects.equals(playerColor, "WHITE")) {
            currentGame = new GameData(gameID, username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game());
        } else if (Objects.equals(playerColor, "BLACK")) {
            currentGame = new GameData(gameID, currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game());
        }
        games.put(gameID, currentGame);
    }

    @Override
    public void clear() {
        games.clear();
    }

    public static int generateID() {
        return new Random().nextInt(1000, 10000);
    }
}
