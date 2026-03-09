package dataaccess;

import model.UserData;
import service.AlreadyTakenException;
import service.ResponseException;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    @Override
    public void createUser(String username, String password, String email) throws AlreadyTakenException, DataAccessException {
        // Hash password
        configureDatabase();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }

    private final String[] createUserTable = {
            """
        CREATE TABLE IF NOT EXISTS user (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        )
        """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

}
