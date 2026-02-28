package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void createGame(String gameName) throws DataAccessException {
        int gameID = Integer.parseInt(generateID());
        // so they're all unique
        while (games.containsKey(gameID)) {
            gameID = Integer.parseInt(generateID());
        }

        GameData gameData = new GameData(gameID, "", "", gameName, new ChessGame());
        games.put(gameID, gameData);
    }

    @Override
    public GameData getGame(int GameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        Collection<GameData> gameList = new ArrayList<>();
        for (int key : games.keySet()) {
            gameList.add(games.get(key));
        }
        return gameList;
    }

    @Override
    public void updateGame(int GameID) throws DataAccessException {

    }

    @Override
    public void deleteGame(int GameID) throws DataAccessException {

    }

    @Override
    public void clear() {
        games.clear();
    }

    public static String generateID() {
        return UUID.randomUUID().toString();
    }
}
