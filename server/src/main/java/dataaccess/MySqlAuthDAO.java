package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO {
    @Override
    public void createAuth(String authToken, String username) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() {

    }

    public void setupAuthTable() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createAuthTable = {
        """
        CREATE TABLE IF NOT EXISTS auth (
            auth_token VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (auth_token),
            FOREIGN KEY(username) REFERENCES user(username)
        )
        """
    };

    //TODO: put in other directory? Serialize error message
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createAuthTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

}
