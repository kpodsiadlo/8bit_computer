package com.kpodsiadlo.eightbitcomputer.server;

import com.kpodsiadlo.eightbitcomputer.handler.ComputerSessionHandler;
import com.kpodsiadlo.eightbitcomputer.json.Utils;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

@ApplicationScoped
@ServerEndpoint("/computer")
public class ComputerWebsocketServer {
    @Inject
    private ComputerSessionHandler sessionHandler;

    @OnOpen
    public void onOpen(Session session) {
        Logger.getLogger(this.getClass().getName()).info("onOpen");
        sessionHandler.addSession(session);
    }

    @OnClose
    public void onClose(Session session) {
        Logger.getLogger(this.getClass().getName()).info("onClose");

        sessionHandler.removeSession(session);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        Logger.getLogger("onMessage").info("onMessage");
        JsonObject jsonMessage = Utils.getJsonObject(message);
        Logger.getLogger("afterGettingJsonObject").info(jsonMessage.toString());
        sessionHandler.updateComputer(jsonMessage);
        sendUpdatedComputerToAllSessions();
    }

    private void sendUpdatedComputerToAllSessions() {
        JsonObject computerState = sessionHandler.getComputerState();
        sessionHandler.sendToAllSessions(computerState);
    }

    @OnError
    public void onError(Throwable error){
        Logger.getLogger(this.getClass().getName()).severe("On Error");
        Logger.getLogger(this.getClass().getName()).severe(error.getMessage());
    }
}
