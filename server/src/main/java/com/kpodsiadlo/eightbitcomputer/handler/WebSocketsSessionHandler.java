package com.kpodsiadlo.eightbitcomputer.handler;

import com.kpodsiadlo.eightbitcomputer.engine.EngineControlMessage;
import com.kpodsiadlo.eightbitcomputer.engine.EngineRestClient;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.ServerMessage;
import com.kpodsiadlo.eightbitcomputer.messages.IdDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class WebSocketsSessionHandler  {

    private final Map<String, WebsocketSession> sessions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public void addSession(Session session) {
        WebsocketSession websocketSession = new WebsocketSession(session);
        websocketSession.addMessageHandler(new ComputerMessageHandler(sessions));

        String clientId = UUID.randomUUID().toString();
        websocketSession.setOriginId(clientId);
        sessions.put(clientId, websocketSession);

        IdDispatcher idDispatcher = new IdDispatcher();
        idDispatcher.sendIdToClient(websocketSession, ServerMessage.originAssignment,
                clientId);
    }

    public void removeSession(Session session) {

        WebsocketSession websocketSession = null;
        for (Map.Entry<String, WebsocketSession> UUIDandSessionEntry : sessions.entrySet()) {
            String websocketSessionId = UUIDandSessionEntry.getValue().getId();
            if (websocketSessionId.equals(session.getId())) {
                websocketSession = UUIDandSessionEntry.getValue();
                assert (websocketSession != null);
                sessions.remove(websocketSession.getOriginId());
                // If webpage, disconnect engine
                if (websocketSession.getSource().equals(MessageSource.WEBPAGE)) {
                    EngineRestClient.sendControlMessage(
                            EngineControlMessage.STOP,
                            websocketSession.getOriginId());
                }
            }
        }
    }

}




