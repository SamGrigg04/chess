package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public final HashMap<Integer, ArrayList<Session>> connections = new HashMap<>();
    public final ArrayList<Session> sessions = new ArrayList<>();
    public final Gson gson = new Gson();

    public void add(Integer gameID, Session session) {
        ArrayList<Session> sessions = connections.get(gameID);
        sessions.add(session);
        connections.put(gameID, sessions);
    }

    public void remove(Integer gameID, Session session) {
        // using the wrong list?
        sessions.remove(session);
        connections.remove(gameID);
    }

    public void sendToOne(Session session, ServerMessage message) throws IOException {
        if (session.isOpen()) {
            session.getRemote().sendString(new Gson().toJson(message.toString()));
        }
    }

    public void sendToOthers(Integer gameID, Session session, ServerMessage message) {

    }

    public void sendToGame(Integer gameID, Session session, ServerMessage message) {

    }

    //TODO: split to three methods
    public void broadcast(Session rootSession, ServerMessage notification, UserGameCommand command) throws IOException {
        String msg = notification.toString();

        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            //TODO: connect sends LOAD_GAME to the sender and Notification to the other clients
            ArrayList<Session> sessions = connections.get(command.getGameID()); // could be null on first add
            for (Session c : sessions) {
                if (c.isOpen()) {
                    if (!c.equals(rootSession)) {
                        c.getRemote().sendString(new NotificationMessage(ServerMessageType.NOTIFICATION).toString()); //needs to be cereal
                    } else {
                        c.getRemote().sendString(new Gson().toJson(msg)); // needs to be cereal
                    }
                }
            }
        } else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            //TODO: leave sends a Notification to all other clients

        } else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            //TODO: resign sends a Notification to all clients

        } else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            //TODO: makeMove sends LOAD_GAME to all clients with an updated game

        } else {
            //TODO: Send an Error to the root
        }

    }



}
