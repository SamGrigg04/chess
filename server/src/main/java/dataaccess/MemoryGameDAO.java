package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void createGame(String gameName) throws DataAccessException {

    }

    @Override
    public GameData getGame(int GameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
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
}
