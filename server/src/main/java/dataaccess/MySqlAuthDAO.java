package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO {
    @Override
    public void createAuth(String authToken, String username) throws DataAccessException {
        var statement = "INSERT INTO auth VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT auth_token, username FROM auth WHERE auth_token=?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeQuery();
                try (var keys = preparedStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return new AuthData(keys.getString("authToken"), keys.getString("username"));
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE auth_token FROM auth WHERE auth_token=?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
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

    private void configureDatabase() throws DataAccessException {
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
