package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public Integer createGame(String gameName) {
        int gameID = generateID();
        // so they're all unique
        while (games.containsKey(gameID)) {
            gameID = generateID();
        }

        GameData gameData = new GameData(gameID, "", "", gameName, new ChessGame());
        games.put(gameID, gameData);

        return gameID;
    }

    @Override
    public GameData getGame(Integer GameID) {
        for (Integer key : games.keySet()) {
            if (Objects.equals(games.get(key).gameID(), GameID)) {
                return games.get(key);
            }
        }
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
    public void updateGame(Integer GameID, String playerColor, String username) throws DataAccessException {
        GameData currentGame = getGame(GameID);
        if (Objects.equals(playerColor, "WHITE")) {
            currentGame = new GameData(GameID, username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game());
        } else if (Objects.equals(playerColor, "BLACK")) {
            currentGame = new GameData(GameID, currentGame.whiteUsername(), username, currentGame.gameName(), currentGame.game());
        }

    }

    @Override
    public void clear() {
        games.clear();
    }

    public static int generateID() {
        return new Random().nextInt(1000, 10000);
    }
}
