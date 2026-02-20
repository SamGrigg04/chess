package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    void createUser(UserData u) throws DataAccessException;
    UserData getUser(UserData u) throws DataAccessException;
    void updateUser(UserData u) throws DataAccessException;
    void deleteUser(UserData u) throws DataAccessException;
    void clear();

}
