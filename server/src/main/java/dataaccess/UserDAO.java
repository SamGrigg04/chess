package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(String username, String password, String email) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void updateUser(String username) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
}
