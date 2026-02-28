package service;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.AuthResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthResult register(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        UserData userData = userDAO.getUser(username);
        if (userData != null) {
            throw new AlreadyTakenException("username already taken");
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
        authDAO.createAuth(authToken, username);

        return new AuthResult(username, authToken);
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (!Objects.equals(authData.authToken(), authToken)) {
            throw new UnauthorizedException("unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
