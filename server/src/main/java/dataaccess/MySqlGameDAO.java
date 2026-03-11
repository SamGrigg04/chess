package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO {
    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        configureDatabase();
        return 0;
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(Integer gameID, String playerColor, String username) throws DataAccessException {

    }

    @Override
    public void clear() {

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

    //TODO: put in other directory? Serialize error message
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
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


//    private ChessGame readChessGame(ResultSet rs) throws SQLException {
//        var chessBoard = rs.getArray("chessBoard");
//        var json = rs.getString("json");
//        var chessGame = new Gson().fromJson(json, ChessGame.class);
//        return chessGame.setBoard(chessGame);
//    }
}
