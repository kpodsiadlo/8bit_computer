package com.kpodsiadlo.eightbitcomputer.server;

import com.kpodsiadlo.eightbitcomputer.handler.ComputerSessionHandler;
import com.kpodsiadlo.eightbitcomputer.json.JsonUtils;
import org.slf4j.Logger;
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
import java.util.Optional;

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
    public void onMessage(String message, Session originSession) {
        logger.info("OnMessage");
        logger.info("Received: {}",message);
        JsonObject jsonMessage = JsonUtils.getJsonObject(message);
        IncomingMessageType messageType = getJsonMessageType(jsonMessage);
        if (messageType.equals(IncomingMessageType.UPDATE)) {
            sessionHandler.updateComputerWithJackson(jsonMessage);
            sessionHandler.sendComputerToReceivingSessions(originSession);
        } else if (messageType.equals(IncomingMessageType.ERROR)) {
            logErrorMessage(message, originSession);
        } else {
            sessionHandler.forwardMessage(message, originSession);
        }
    }

    @OnError
    public void onError(Throwable error) {
        logger.error("On Error: {}", error.getMessage());
    }

    private boolean checkForKeyInMessage(JsonObject message, String key) {
        Optional<String> keyInJson = Optional.empty();
        try {
            keyInJson = Optional.of(message.getString(key));
        } catch (NullPointerException ignored) {
            logger.debug("Message is not a {}", key);
        }
        return keyInJson.isPresent();
    }

    private IncomingMessageType getJsonMessageType(JsonObject message) {
        if (checkForKeyInMessage(message, "type")) {
            String type = message.getString("type");
            logger.info(type);
            return IncomingMessageType.valueOf(type.toUpperCase());
        } else
            return IncomingMessageType.ERROR;
    }

    private void logErrorMessage(String message, Session originSession) {
        logger.error("ERROR - no message type in JSON. \nMessage: {}, Session: {}",message, originSession.getId());
    }


    /*    private void sendPingToReceivingSessions(Session session) {
            logger.debug("sendPingToReceivingSessions");
            sessionHandler.sendToAllReceivingSessions(generateControlMessage("ping"), session);
        }

        private void sendRamUpdateToReceivingSessions(Session session) {
            logger.info("SendRamUpdateToReceivingSessions");
            JsonObjectBuilder objectBuilder = JsonProvider.provider().createObjectBuilder();
            objectBuilder.add("SOURCE", "Server");
            objectBuilder.add("ramUpdate", true);
            objectBuilder.add("memoryContents", sessionHandler.getRamContents());
            sessionHandler.sendToAllReceivingSessions(objectBuilder.build().toString(), session);
        }

        private void sendResetEngineToReceivingSessions(Session session) {
            logger.debug("SendResetToReceivingSessions");
            sessionHandler.sendToAllReceivingSessions(generateControlMessage("reset"), session);
        }

        private void sendExecuteOneClockCycleToRecevingSessions(Session session) {
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


     */
}
