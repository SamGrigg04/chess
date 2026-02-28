package dataaccess;

import model.UserData;
import service.AlreadyTakenException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(String username, String password, String email) throws AlreadyTakenException {
        UserData newUser = new UserData(username, password, email);
        users.put(username, newUser);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException  {
        for (String key : users.keySet()) {
            if (Objects.equals(users.get(key).username(), username)) {
                return users.get(key);
            }
        }
        return null;
    }

    @Override
    public void updateUser(String username) throws DataAccessException {

    }

    @Override
    public void deleteUser(String username) throws DataAccessException {

    }

    @Override
    public void clear() {
        users.clear();
    }
}
