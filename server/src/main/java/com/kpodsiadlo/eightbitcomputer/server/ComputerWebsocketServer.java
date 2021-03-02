package com.kpodsiadlo.eightbitcomputer.server;

import com.kpodsiadlo.eightbitcomputer.handler.ComputerSessionHandler;
import com.kpodsiadlo.eightbitcomputer.json.Utils;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/computer")
public class ComputerWebsocketServer {
    @Inject
    private ComputerSessionHandler sessionHandler;

    @OnOpen
    public void onOpen(Session session) {
        LoggerFactory.getLogger(this.getClass()).debug("OnOpen");
        sessionHandler.addSession(session);
    }

    @OnClose
    public void onClose(Session session) {
        LoggerFactory.getLogger(this.getClass()).debug("OnClose");
        if (session != null) {
            sessionHandler.removeSession(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LoggerFactory.getLogger(this.getClass()).info("OnMessage");
        LoggerFactory.getLogger(this.getClass()).info(message);
        JsonObject jsonMessage = Utils.getJsonObject(message);
        sessionHandler.updateComputerModel(jsonMessage);
        sendUpdatedComputerToAllSessions();
    }

    private void sendUpdatedComputerToAllSessions() {
        LoggerFactory.getLogger(this.getClass()).info("sendUpdatedComputerToAllSessions");
        JsonObject computerState = sessionHandler.getComputerModelState();
        sessionHandler.sendToAllSessions(computerState);
    }

    @OnError
    public void onError(Throwable error) {
        LoggerFactory.getLogger(this.getClass()).error("On Error " + error.getMessage());
    }
}
