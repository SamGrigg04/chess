package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public final HashMap<Integer, ArrayList<Session>> connections = new HashMap<>();
    public final ArrayList<Session> sessions = new ArrayList<>();

    public void add(Integer gameID, Session session) {
        sessions.add(session);
        connections.put(gameID, sessions);
    }

    public void remove(Integer gameID, Session session) {
        sessions.remove(session);
        connections.remove(gameID);
    }

    public void broadcast(Session excludeSession, ServerMessage notification) throws IOException {
        //TODO
    }


}
