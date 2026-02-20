package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData u) throws DataAccessException;
    AuthData getAuth(AuthData u) throws DataAccessException;
    void updateAuth(AuthData u) throws DataAccessException;
    void deleteAuth(AuthData u) throws DataAccessException;
    void clear();
}
