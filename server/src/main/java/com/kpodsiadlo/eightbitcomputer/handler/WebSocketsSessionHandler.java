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
        sendToRelatedSessions(message, originSession);
    }


    public void addSession(Session session) {
        String sessionIdFromEngine = session.getNegotiatedSubprotocol();
        if (sessionIdFromEngine.equals("")) {
            logger.info("Browser registering");
            String sessionId = session.getId();
            addSessionToSessions(sessionId, session);
            String sessionIdJson = "{\"sessionId\": \"" + sessionId + "\"}";
            sendToSession(session, sessionIdJson);

        } else {
            logger.info("Engine registering");
            addSessionToSessions(sessionIdFromEngine, session);
        }
    }

    private void addSessionToSessions(String sessionId, Session session) {
        try {
            Set<Session> commonSessions = sessions.get(sessionId);
            commonSessions.add(session);
        } catch (NullPointerException e) {
            Set<Session> newCommonSessionsSet = new HashSet<>();
            newCommonSessionsSet.add(session);
            this.sessions.put(sessionId, newCommonSessionsSet);
        }
    }

    public void removeSession(Session session) {
        String sessionIdFromEngine = session.getNegotiatedSubprotocol();
        if (sessionIdFromEngine.equals(" ")) {
            logger.info("Engine closed connection");
            try {
                Set<Session> commonSessions = this.sessions.get(sessionIdFromEngine);
                commonSessions.remove(session);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Browser closed connection");
            try {
                sessions.remove(session.getId());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
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

    public void sendToRelatedSessions(String message, Session originSession) {
        String sessionId = originSession.getNegotiatedSubprotocol();
        if (sessionId.equals("")) {
            sessionId = originSession.getId();
        }
        Set<Session> relatedSessions = new HashSet<>(sessions.get(sessionId));
        relatedSessions.remove(originSession);
        relatedSessions.forEach(session -> sendToSession(session, message));
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