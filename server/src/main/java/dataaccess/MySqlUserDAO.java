package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.AlreadyTakenException;

import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {
    @Override
    public void createUser(String username, String password, String email) throws AlreadyTakenException, DataAccessException {
        var statement = "INSERT INTO user VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM user WHERE username=?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        return new UserData(
                                result.getString("username"),
                                result.getString("password"),
                                result.getString("email")
                        );
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }

    }

    @Override
    public void clear() throws DataAccessException {
        // So it turns out that since there are foreign keys,
        // this one doesn't like to delete. So you have to tell
        // it to ignore that and delete anyway
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.createStatement()) {
            conn.setAutoCommit(false); // makes it so execute happens when I say, not immediately
            try {
                // Is this a transaction?
                statement.execute("SET FOREIGN_KEY_CHECKS=0");
                statement.execute("TRUNCATE TABLE user");
                statement.execute("SET FOREIGN_KEY_CHECKS=1");
                conn.commit(); // Do all the executes now
            } catch (SQLException e) {
                // if something goes wrong, undo everything
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    public void setupUserTable() throws DataAccessException {
        configureDatabase();
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

    private void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

}
