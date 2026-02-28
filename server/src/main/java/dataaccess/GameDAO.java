package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    GameData getGame(Integer GameID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(Integer GameID, String playerColor, String username) throws DataAccessException;
    void deleteGame(Integer GameID) throws DataAccessException;
    void clear();
}
