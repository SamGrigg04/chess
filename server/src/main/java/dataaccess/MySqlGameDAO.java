package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MySqlGameDAO implements GameDAO {
    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (game_name, game_state) VALUES (?, ?)";
        String json = new Gson().toJson(new ChessGame());

        try (var conn = DatabaseManager.getConnection();
             // RETURN_GENERATED_KEYS allows us to call .getGeneratedKeys to get
             // a ResultSet that contains the generated columns (game_id) for the
             // inserted rows
             var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, json);
            preparedStatement.executeUpdate();

            try (var keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new DataAccessException("Unable to read game_id");
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
                preparedStatement.executeQuery();
                try (var keys = preparedStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return new GameData(
                                keys.getInt(1),
                                keys.getString(2),
                                keys.getString(3),
                                keys.getString(4),
                                new Gson().fromJson(keys.getString(5), ChessGame.class)
                                );
                    }
                    throw new DataAccessException("Unable to read game_id");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var statement = "SELECT * FROM game";
        ArrayList<GameData> gameList = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while (result.next()) {
                        gameList.add(new GameData(
                                result.getInt("game_id"),
                                result.getString("white_username"), // could be null
                                result.getString("black_username"), // could be null
                                result.getString("game_name"),
                                new Gson().fromJson(result.getString("game_state"), ChessGame.class)
                        ));
                    }
                    return gameList;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public void updateGame(Integer gameID, String playerColor, String username) throws DataAccessException {
        String statement;

        if (Objects.equals(playerColor, "WHITE")) {
            statement = "UPDATE game SET white_username = ? WHERE game_id=?";
        } else if (Objects.equals(playerColor, "BLACK")) {
            statement = "UPDATE game SET black_username = ? WHERE game_id=?";
        } else {
            throw new DataAccessException("Invalid player color");
        }
        
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
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
