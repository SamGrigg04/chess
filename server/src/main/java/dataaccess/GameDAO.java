package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(String gameName) throws DataAccessException;
    GameData getGame(int GameID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(int GameID) throws DataAccessException;
    void deleteGame(int GameID) throws DataAccessException;
    void clear();
}
