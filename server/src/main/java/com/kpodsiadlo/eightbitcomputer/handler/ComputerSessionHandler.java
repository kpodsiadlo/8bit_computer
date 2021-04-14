package com.kpodsiadlo.eightbitcomputer.handler;

import com.kpodsiadlo.eightbitcomputer.json.JsonUtils;
import com.kpodsiadlo.eightbitcomputer.server.IncomingMessageType;
import com.kpodsiadlo.eightbitcomputer.service.ComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class ComputerSessionHandler {

    private final Set<Session> sessions = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    ComputerService computerService;


    public void sendComputerToReceivingSessions(Session transmittingSession) {
        logger.info("sendUpdatedComputerToReceivingSessions");
        String computerState = computerService.getComputerStateAsJson();
        sendToAllReceivingSessions(computerState, transmittingSession);
    }

    public void forwardMessage(String message, Session originSession) {
        sendToAllReceivingSessions(message, originSession);
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            removeSession(session);
            logger.error(e.getMessage());
        }
    }

    public void sendToAllReceivingSessions(String message, Session transmittingSession) {
        logger.info("Sending: {}", message);
        Set<Session> receivingSessions = new HashSet<>(sessions);
        receivingSessions.remove(transmittingSession);
        receivingSessions.forEach(session -> sendToSession(session, message));

    }

    public void processMessage(String message, Session originSession) {
        logger.info("OnMessage");
        logger.info("Received: {}",message);
        JsonObject jsonMessage = JsonUtils.getJsonObject(message);
        IncomingMessageType messageType = getJsonMessageType(jsonMessage);
        if (messageType.equals(IncomingMessageType.UPDATE)) {
            computerService.updateComputerWithJackson(jsonMessage);
            sendComputerToReceivingSessions(originSession);
        } else if (messageType.equals(IncomingMessageType.ERROR)) {
            logErrorMessage(message, originSession);
        } else {
            forwardMessage(message, originSession);
        }
    }

    private IncomingMessageType getJsonMessageType(JsonObject message) {
        if (checkForKeyInMessage(message, "type")) {
            String type = message.getString("type");
            logger.info(type);
            return IncomingMessageType.valueOf(type.toUpperCase());
        } else
            return IncomingMessageType.ERROR;
    }

    private boolean checkForKeyInMessage(JsonObject message, String key) {
        Optional<String> keyInJson = Optional.empty();
        try {
            keyInJson = Optional.of(message.getString(key));
        } catch (NullPointerException ignored) {
            logger.debug("No \"{}\" key in incoming Message", key);
        }
        return keyInJson.isPresent();
    }

    private void logErrorMessage(String message, Session originSession) {
        logger.error("ERROR - no message type in JSON. \nMessage: {}, Session: {}",message, originSession.getId());
    }

    public void sendToAllSessions(String message) {
        sessions.forEach(session -> sendToSession(session, message));
    }

    public JsonArray getRamContents() {
        logger.info("getRamContents");
        List<Integer> memoryContents = computerService.getComputerMemoryContents();
        JsonObjectBuilder objectBuilder = JsonProvider.provider().createObjectBuilder();
        JsonArrayBuilder arrayBuilder = JsonProvider.provider().createArrayBuilder();
        for (int i = 0; i < memoryContents.size(); i++) {
            arrayBuilder.add(memoryContents.get(i));
        }
        return arrayBuilder.build();
    }
}