package com.kpodsiadlo.eightbitcomputer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class ComputerSessionHandler {

    private final Set<Session> sessions = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    ComputerMessageHandler messageHandler;

    public void forwardMessage(String message, Session originSession) {
        sendToAllOtherSessions(messageHandler.processMessage(message), originSession);
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

    public void sendToAllOtherSessions(String message, Session transmittingSession) {
        logger.debug("Sending: {}", message);
        Set<Session> receivingSessions = new HashSet<>(sessions);
        receivingSessions.remove(transmittingSession);
        receivingSessions.forEach(session -> sendToSession(session, message));

    }

    public void sendToAllSessions(String message) {
        sessions.forEach(session -> sendToSession(session, message));
    }


}