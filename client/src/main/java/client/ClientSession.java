package client;

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
    public void startGame() {}

    public void leaveGame() {
        activeGameID = null;
        activePerspective = null;
        observer = false;
        currentGame = null;
        if (state != State.SIGNEDOUT) {
            state = State.SIGNEDIN;
        }
    }
    public void updateGame() {}

    public void assertSignedIn() {}

    public void assertPlaying() {}

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
