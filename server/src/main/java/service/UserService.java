package service;

import request.LoginRequest;
import request.RegisterRequest;
import result.AuthResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthResult register(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        UserData userData = userDAO.getUser(username);
        if (userData != null) {
            throw new AlreadyTakenException("already taken");
        }

        userDAO.createUser(username, password, email);
        String authToken = generateToken();
        authDAO.createAuth(authToken, username);

        return new AuthResult(username, authToken);
    }

    public AuthResult login(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();

        UserData userData = userDAO.getUser(username);
        if (userData == null) {
            throw new UnauthorizedException("unauthorized");
        }
        if (!Objects.equals(userData.password(), password)) {
            throw new UnauthorizedException("unauthorized");
        }

        String authToken = generateToken();
        // avoid duplicates
        while (authDAO.getAuth(authToken) != null) {
            authToken = generateToken();
        }
        authDAO.createAuth(authToken, username);

        return new AuthResult(username, authToken);
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null || !Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
