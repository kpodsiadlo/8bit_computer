package com.kpodsiadlo.eightbitcomputer.handler;

import com.kpodsiadlo.eightbitcomputer.engine.EngineControlMessage;
import com.kpodsiadlo.eightbitcomputer.engine.EngineRestClient;
import com.kpodsiadlo.eightbitcomputer.json.StringSerializer;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.ServerMessage;
import com.kpodsiadlo.eightbitcomputer.messages.Message;
import com.kpodsiadlo.eightbitcomputer.messages.MessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class WebSocketsSessionHandler {

    private final Map<String, WebsocketSession> sessions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public void addSession(Session session) {
        WebsocketSession websocketSession = new WebsocketSession(session);
        websocketSession.addMessageHandler(new ComputerMessageHandler(sessions));

        String clientId = UUID.randomUUID().toString();
        websocketSession.setOriginId(clientId);
        sessions.put(clientId, websocketSession);

        Message idAssignMessage = MessageFactory.getIdAssignMessage(ServerMessage.originAssignment, clientId);
        MessageSender.sendToSession(StringSerializer.convertToJsonString(idAssignMessage),
            websocketSession);
    }

    public void removeSession(Session session) {

        for (WebsocketSession websocketSession: sessions.values()) {
            if (websocketSession.getId().equals(session.getId())) {
                sessions.remove(websocketSession.getOriginId());

                // If webpage, disconnect the engine
                if (websocketSession.getSource().equals(MessageSource.WEBPAGE)) {
                    EngineRestClient.sendControlMessage(
                            EngineControlMessage.STOP,
                            websocketSession.getOriginId());
                }
                return;
            }
        }
    }

    public boolean sessionExist(String uuid) {
        boolean contains = sessions.containsKey(uuid);
        return contains;
    }

}




