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

//    private ChessGame readChessGame(ResultSet rs) throws SQLException {
//        var chessBoard = rs.getArray("chessBoard");
//        var json = rs.getString("json");
//        var chessGame = new Gson().fromJson(json, ChessGame.class);
//        return chessGame.setBoard(chessGame);
//    }
}
