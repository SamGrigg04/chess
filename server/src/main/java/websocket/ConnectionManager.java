package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public final HashMap<Integer, ArrayList<Session>> connections = new HashMap<>();
    public final Gson gson = new Gson();

    // Adds a session to the list by gameID. If the gameID doesn't exist, creates a new list for it.
    public void add(Integer gameID, Session session) {
        ArrayList<Session> sessions = connections.get(gameID);
        if (sessions == null) {
            sessions = new ArrayList<>();
        }
        sessions.add(session);
        connections.put(gameID, sessions);
    }

    // Removes a session from the list by gameID. If that list is now empty, it removes the connection.
    // So when the last player/observer leaves, the connection will automatically be deleted
    public void remove(Integer gameID, Session session) {
        ArrayList<Session> sessions = connections.get(gameID);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    // Sends a serialized message to one user (usually root)
    public void sendToOne(Session session, ServerMessage message) throws IOException {
        if (session.isOpen()) {
            session.getRemote().sendString(gson.toJson(message));
        }
    }

    // Sends a serialized message to all users except the one passed in that share a game with root
    public void sendToOthers(Integer gameID, Session session, ServerMessage message) throws IOException {
        ArrayList<Session> sessions = connections.get(gameID);
        if (sessions != null) {
            for (Session c : sessions) {
                if (c.isOpen() && !c.equals(session)) {
                    c.getRemote().sendString(gson.toJson(message));
                }
            }
        }
    }

    // Sends a serialized message to all users in a game with root (including root)
    public void sendToGame(Integer gameID, ServerMessage message) throws IOException {
        ArrayList<Session> sessions = connections.get(gameID);
        if (sessions != null) {
            for (Session c : sessions) {
                if (c.isOpen()) {
                    c.getRemote().sendString(gson.toJson(message));
                }
            }
        }
    }
}
