package com.kpodsiadlo.eightbitcomputer.server;

import com.kpodsiadlo.eightbitcomputer.handler.ComputerSessionHandler;
import com.kpodsiadlo.eightbitcomputer.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @OnOpen
    public void onOpen(Session session) {
        logger.debug("OnOpen");
        sessionHandler.addSession(session);
    }

    @OnClose
    public void onClose(Session session) {
        logger.debug("OnClose");
        if (session != null) {
            sessionHandler.removeSession(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("OnMessage");
        logger.info(message);
        JsonObject jsonMessage = JsonUtils.getJsonObject(message);
        IncomingMessageType messageType = sessionHandler.processJsonMessage(jsonMessage);
        if (messageType.equals(IncomingMessageType.UPDATE)) {
            sendUpdatedComputerToReceivingSessions(session);
        } else if (messageType.equals(IncomingMessageType.TICK)) {
            sendTickToRecevingSessions(session);
        } else if(messageType.equals(IncomingMessageType.RESET)) {
            sendResetToReceivingSessions(session);
        } else if (messageType.equals(IncomingMessageType.RAM_UPDATE)) {
            sendRamUpdateToReceivingSessions(session);
        }
    }

    private void sendRamUpdateToReceivingSessions(Session session) {
        logger.info("SendRamUpdateToReceivingSessions");
        JsonObjectBuilder objectBuilder = JsonProvider.provider().createObjectBuilder();
        objectBuilder.add("SOURCE", "Server");
        objectBuilder.add("memoryContents", sessionHandler.getRamContents());
        sessionHandler.sendToAllReceivingSessions(objectBuilder.build().toString(), session);
    }

    private void sendResetToReceivingSessions(Session session) {
        logger.debug("SendResetToReceivingSessions");
        sessionHandler.sendToAllReceivingSessions(generateControlMessage("reset"), session);
    }

    private void sendTickToRecevingSessions(Session session) {
        logger.debug("SendTickToReceivingSessions");
        sessionHandler.sendToAllReceivingSessions(generateControlMessage("tick"), session);
    }

    private String generateControlMessage(String message) {
        logger.debug(message);
        JsonObjectBuilder objectBuilder = JsonProvider.provider().createObjectBuilder();
        objectBuilder.add("SOURCE", "Server");
        objectBuilder.add(message, true);
        return objectBuilder.build().toString();
    }

    private void sendUpdatedComputerToReceivingSessions(Session transmittingSession) {
        logger.info("sendUpdatedComputerToReceivingSessions");
        String computerState = sessionHandler.getComputerStateAsJson();
        logger.info("Sending: {}", computerState);
        sessionHandler.sendToAllReceivingSessions(computerState,transmittingSession);
    }

    @OnError
    public void onError(Throwable error) {
        logger.error("On Error: {}", error.getMessage());
    }
}
