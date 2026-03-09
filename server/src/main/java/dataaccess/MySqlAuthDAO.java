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

}
