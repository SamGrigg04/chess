package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;
import websocket.commands.*;

import java.io.IOException;


public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (UserGameCommand.getCommandType()) {
                case CONNECT -> connect(command.getAuthToken(), command.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), ctx.session);
                case LEAVE -> leave(command.getAuthToken(), command.getGameID(), ctx.session);
                case RESIGN -> resign(command.getAuthToken(), command.getGameID(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connect(String authToken, Integer gameID, Session session) throws IOException {
        connections.add(gameID, session);
        var message = String.format("%s joined the game", gameID); //TODO: get username? right message?
        var notification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(session, notification);
    }

    public void makeMove(String authToken, Integer gameID, Session session) throws IOException {

    }

    public void leave(String authToken, Integer gameID, Session session) throws IOException {
        var message = String.format("%s left the game", gameID); //TODO: get username? right message?
        var notification = new LoadGameMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, notification);
        connections.remove(gameID, session);
    }

    public void resign(String authToken, Integer gameID, Session session) throws IOException {

    }
}
