package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void createAuth(String authToken, String username) throws DataAccessException {
        AuthData newAuth = new AuthData(authToken, username);
        authTokens.put(authToken, newAuth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (String key : authTokens.keySet()) {
            if (Objects.equals(authTokens.get(key).authToken(), authToken)) {
                return authTokens.get(key);
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authTokens.remove(authToken);
    }

    @Override
    public void clear() {
        authTokens.clear();
    }
}
