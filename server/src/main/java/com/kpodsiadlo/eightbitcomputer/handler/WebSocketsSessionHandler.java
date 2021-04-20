package com.kpodsiadlo.eightbitcomputer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class WebSocketsSessionHandler implements MessageHandler {

    private final Map<String, Set<Session>> sessions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


    public void forwardMessage(String message, Session originSession) {
        sendToAllOtherSessions(message, originSession);
    }


    public void addSession(Session session) {
        Set<Session> commonSessions;
        try {
            commonSessions = this.sessions.get(session.getNegotiatedSubprotocol());
            commonSessions.add(session);
            sessions.put(session.getNegotiatedSubprotocol(), commonSessions);
        } catch (NullPointerException e) {
            Set<Session> newCommonSessionsSet = Set.of(session);
            this.sessions.put(session.getNegotiatedSubprotocol(), newCommonSessionsSet);
        }
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void sendToSession(Session session, String message) {
//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            removeSession(session);
//            logger.error(e.getMessage());
//        }
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