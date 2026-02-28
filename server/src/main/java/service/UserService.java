package service;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.AuthResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public AuthResult register(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        UserData userData = userDAO.getUser(username);
        if (userData != null) {
            throw new AlreadyTakenException("Username already taken");
        }
        userDAO.createUser(username, password, email);
        String authToken = generateToken();
        authDAO.createAuth(authToken, username);

        return new AuthResult(username, authToken);
    }

    public void login(LoginRequest loginRequest) { }

    public void logout() { }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
