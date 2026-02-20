package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData u) throws DataAccessException;
    GameData getGame(GameData u) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(GameData u) throws DataAccessException;
    void deleteGame(GameData u) throws DataAccessException;
    void clear();
}
