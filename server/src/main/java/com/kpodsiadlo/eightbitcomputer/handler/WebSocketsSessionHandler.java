package com.kpodsiadlo.eightbitcomputer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class WebSocketsSessionHandler implements MessageHandler {

    private final Map<String, Session> sessions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public void forwardMessage(String message, Session originSession) {
        sendToAllOtherSessions(message, originSession);
    }

    public void addSession(Session session) {
        String computerId = UUID.randomUUID().toString();
        sessions.put(computerId, session);
        String message = String.format(
                "{\"source\": \"SERVER\", \"type\": \"idAssigment\", \"id\":\"%s\"}",
                computerId);
        sendToSession(session, message);
    }

    public void removeSession(Session session) {
        String computerId = null;
        for (Map.Entry<String, Session> stringSessionEntry : sessions.entrySet()) {
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

    public void sendToAllOtherSessions(String message, Session originSession) {
        logger.debug("Sending: {}", message);
        Set<Session> receivingSessions = new HashSet<>(sessions.values());
        receivingSessions.remove(originSession);
        receivingSessions.forEach(session -> sendToSession(session, message));

    }

//    public void sendToAllSessions(String message) {
//        sessions.forEach(session -> sendToSession(session, message));
//    }


}