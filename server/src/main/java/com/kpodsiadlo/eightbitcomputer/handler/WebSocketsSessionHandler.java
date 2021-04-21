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

@ApplicationScoped
public class WebSocketsSessionHandler implements MessageHandler {

    private final Map<String, Set<Session>> sessions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    public void forwardMessage(String message, Session originSession) {
        sendToSessionsWithTheSameUUID(message, originSession);
    }


    public void addSession(Session session) {
        String uuid = session.getNegotiatedSubprotocol();
        try {
            Set<Session> sessionsWithTheSameUUID = this.sessions.get(uuid);
            sessionsWithTheSameUUID.add(session);
        } catch (NullPointerException e) {
            Set<Session> newCommonSessionsSet = new HashSet<Session>();
            newCommonSessionsSet.add(session);
            this.sessions.put(uuid, newCommonSessionsSet);
        }
    }

    public void removeSession(Session session) {
        String uuid = session.getNegotiatedSubprotocol();
        Set<Session> sessionsWithTheSameUUID = this.sessions.get(uuid);
        sessionsWithTheSameUUID.remove(session);
        if (sessionsWithTheSameUUID.isEmpty()) {
            this.sessions.remove(uuid);
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

    public void sendToSessionsWithTheSameUUID(String message, Session originSession) {
        String uuid = originSession.getNegotiatedSubprotocol();
        Set<Session> sessionsWithTheSameUUID = new HashSet<Session>(sessions.get(uuid));
        sessionsWithTheSameUUID.remove(originSession);
        sessionsWithTheSameUUID.forEach(session -> sendToSession(session, message));
    }

    public void sendToAllOtherSessions(String message, Session originSession) {
//        logger.debug("Sending: {}", message);
//        Set<Session> receivingSessions = new HashSet<>(sessions);
//        receivingSessions.remove(originSession);
//        receivingSessions.forEach(session -> sendToSession(session, message));

    }

    public void sendToAllSessions(String message) {
//        sessions.forEach(session -> sendToSession(session, message));
    }


}