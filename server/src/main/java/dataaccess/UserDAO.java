package dataaccess;

import model.UserData;
import service.AlreadyTakenException;

public interface UserDAO {
    void createUser(String username, String password, String email) throws AlreadyTakenException;
    UserData getUser(String username) throws DataAccessException;
    void clear();
}
