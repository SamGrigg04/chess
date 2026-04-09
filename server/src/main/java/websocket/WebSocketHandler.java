package websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.messages.*;
import websocket.commands.*;
import websocket.result.ConnectResult;
import websocket.result.*;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final WebSocketService websocketService;

    public WebSocketHandler(WebSocketService websocketService) {
        this.websocketService = websocketService;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> {
                    ConnectResult result;
                    try {
                        result = websocketService.connect(command);
                    } catch (ResponseException e) {
                        connections.sendToOne(ctx.session,
                                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                                        e.getMessage()));
                        return;
                    }
                    connections.add(command.getGameID(), ctx.session);
                    connections.sendToOne(ctx.session,
                            new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, result.game()));
                    connections.sendToOthers(command.getGameID(), ctx.session,
                            new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                    result.notificationText()));
                }
                case MAKE_MOVE -> {
                    MoveResult result;
                    try {
                        result = websocketService.makeMove(command);
                    } catch (ResponseException e) {
                        connections.sendToOne(ctx.session,
                                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                                        e.getMessage()));
                        return;
                    }
                    connections.sendToGame(command.getGameID(),
                            new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, result.game()));
                    connections.sendToOthers(command.getGameID(), ctx.session,
                            new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                    result.moveNotification()));
                    // if check, checkmate, or stalemate
                    if (result.statusNotification() != null && !result.statusNotification().isEmpty()) {
                        connections.sendToGame(command.getGameID(),
                                new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                        result.statusNotification()));
                    }
                }
                case LEAVE -> {
                    LeaveResult result;
                    try {
                        result = websocketService.leave(command);
                    } catch (ResponseException e) {
                        connections.sendToOne(ctx.session,
                                new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
                        return;
                    }
                    connections.sendToOthers(command.getGameID(), ctx.session,
                            new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                    result.notificationText()));
                    connections.remove(command.getGameID(), ctx.session);
                }
                case RESIGN -> {
                    ResignResult result;
                    try {
                        result = websocketService.resign(command);
                    } catch (ResponseException e) {
                        connections.sendToOne(ctx.session,
                                new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage()));
                        return;
                    }
                    connections.sendToOthers(command.getGameID(), ctx.session,
                            new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                    result.notificationText()));
                }
            }
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

}
