package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO {
    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (game_name, game_state) VALUES (?, ?)";
        var statement2 = "SELECT game_id FROM game WHERE game_name=?";
        String json = new Gson().toJson(new ChessGame());

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, json);
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(statement2)) {
                preparedStatement.setString(1, gameName);
                var result = preparedStatement.executeQuery();

                return result.getInt("game_id");
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        var statement = "SELECT * FROM game WHERE game_id=?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                var result = preparedStatement.executeQuery();

                return new GameData(
                        result.getInt("game_id"),
                        result.getString("white_username"), // could be null
                        result.getString("black_username"), // could be null
                        result.getString("game_name"),
                        new Gson().fromJson(result.getString("game_state"), ChessGame.class)
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(Integer gameID, String playerColor, String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    public void setupGameTable() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createGameTable = {
            """
        CREATE TABLE IF NOT EXISTS game (
            game_id INT AUTO_INCREMENT NOT NULL,
            game_name VARCHAR(255) NOT NULL,
            white_username VARCHAR(255),
            black_username VARCHAR(255),
            game_state JSON NOT NULL,
            PRIMARY KEY (game_id)
        )
        """
    };

    private void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createGameTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

}
