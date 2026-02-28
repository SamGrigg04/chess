package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
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
}
