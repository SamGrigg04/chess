package client;

import exception.ResponseException;
import model.GameData;
import ui.BoardPerspective;

public class ClientSession {
    private String visitorName;
    private String authToken;
    private State state = State.SIGNEDOUT;
    private Integer activeGameID;
    private BoardPerspective activePerspective;
    private boolean observer;
    private GameData currentGame;

    public void signIn(String username, String token) {
        visitorName = username;
        authToken = token;
        state = State.SIGNEDIN;
    }

    public void signOut() {
        visitorName = null;
        authToken = null;
        state = State.SIGNEDOUT;
        leaveGame();
    }

    public void startGame(Integer gameID, BoardPerspective perspective, boolean isObserver, GameData game) {
        activeGameID = gameID;
        activePerspective = perspective;
        observer = isObserver;
        currentGame = game;
        state = State.PLAYING;
    }

    public void leaveGame() {
        activeGameID = null;
        activePerspective = null;
        observer = false;
        currentGame = null;
        if (state != State.SIGNEDOUT) {
            state = State.SIGNEDIN;
        }
    }
    public void updateGame(GameData game) {
        currentGame = game;
    }

    public void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }

    public void assertPlaying() throws ResponseException {
        if (state != State.PLAYING) {
            throw new ResponseException("You are not currently in a game");
        }
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public State getState() {
        return state;
    }

    public Integer getActiveGameID() {
        return activeGameID;
    }

    public BoardPerspective getActivePerspective() {
        return activePerspective;
    }

    public boolean isObserver() {
        return observer;
    }

    public GameData getCurrentGame() {
        return currentGame;
    }

}
