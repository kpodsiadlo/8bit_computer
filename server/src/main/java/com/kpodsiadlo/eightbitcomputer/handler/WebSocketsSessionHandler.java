package com.kpodsiadlo.eightbitcomputer.handler;

import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.ServerMessage;
import com.kpodsiadlo.eightbitcomputer.messages.IdDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
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

        IdDispatcher idDispatcher = new IdDispatcher();
        idDispatcher.sendIdToClient(websocketSession, ServerMessage.originAssignment,
                clientId);
    }

    public void removeSession(Session session) {

        WebsocketSession websocketSession = null;
        for (Map.Entry<String, WebsocketSession> stringSessionEntry : sessions.entrySet()) {
            // Find enclosing websocketsession based on session_id
            if (stringSessionEntry.getValue().getId().equals(session.getId())) {
                websocketSession = stringSessionEntry.getValue();
                assert (websocketSession != null);
                sessions.remove(websocketSession.getOriginId());
                // If webpage, disconnect engine (for safety)
                if (websocketSession.getSource().equals(MessageSource.WEBPAGE)) {
                    sessions.remove(websocketSession.getTargetId());
                }
            }
        }
        assert websocketSession != null;
    }

}




