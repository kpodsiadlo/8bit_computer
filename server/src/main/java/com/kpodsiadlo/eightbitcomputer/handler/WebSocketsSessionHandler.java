package com.kpodsiadlo.eightbitcomputer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpodsiadlo.eightbitcomputer.handler.messages.MessageSource;
import com.kpodsiadlo.eightbitcomputer.handler.messages.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class WebSocketsSessionHandler implements MessageHandler {

    private final Map<String, WebsocketSession> sessions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public void addSession(Session session) {
        WebsocketSession websocketSession = new WebsocketSession(session);
        websocketSession.addMessageHandler(new ComputerMessageHandler(session, sessions));
        String clientId = UUID.randomUUID().toString();
        websocketSession.setOriginId(clientId);
        sessions.put(clientId, websocketSession);
        sendIdToClient(websocketSession, clientId);
    }

    public void removeSession(Session session) {
        String computerId = null;
        for (Map.Entry<String, WebsocketSession> stringSessionEntry : sessions.entrySet()) {
            if (stringSessionEntry.getValue().getId().equals(session.getId())) {
                computerId = stringSessionEntry.getKey();
            }
        }
        if (computerId != null) {
            sessions.remove(computerId);
        }
    }

    public void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            removeSession(session);
            logger.error(e.getMessage());
        }
    }




    private void sendIdToClient(Session session, String clientId) {
        IdAssignMessage idAssignMessage = getIdAssignMessage(clientId);
        String jsonMessage = convertToJsonMessage(idAssignMessage);
        sendToSession(session, jsonMessage);
    }

    private String convertToJsonMessage(MessageHeader message) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = null;
        try {
            jsonMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonMessage;
    }

    private IdAssignMessage getIdAssignMessage(String clientId) {
        IdAssignMessage idAssignMessage = new IdAssignMessage();
        idAssignMessage.setSource(MessageSource.SERVER);
        idAssignMessage.setType(ServerMessage.idAssignment);
        idAssignMessage.setId(clientId);
        return idAssignMessage;
    }
}

