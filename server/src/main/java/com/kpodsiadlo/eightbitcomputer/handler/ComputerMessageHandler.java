package com.kpodsiadlo.eightbitcomputer.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kpodsiadlo.eightbitcomputer.json.JsonConverter;
import com.kpodsiadlo.eightbitcomputer.messageType.EngineMessage;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.ServerMessage;
import com.kpodsiadlo.eightbitcomputer.messageType.WebpageMessage;
import com.kpodsiadlo.eightbitcomputer.json.MessageHeaderDeserializer;
import com.kpodsiadlo.eightbitcomputer.messages.IdDispatcher;
import com.kpodsiadlo.eightbitcomputer.messages.MessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;


public class ComputerMessageHandler implements MessageHandler.Whole<String> {
    private final Session session;
    private final Map<String, WebsocketSession> sessions;
    private final Logger logger = LoggerFactory.getLogger(ComputerMessageHandler.class);

    public ComputerMessageHandler(Session session, Map<String, WebsocketSession> sessions) {
        this.sessions = sessions;
        this.session = session;
    }

    @Override
    public void onMessage(String message) {
        try {
            processMessage(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void processMessage(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                false);
        mapper.registerModule(new SimpleModule().addDeserializer(MessageHeader.class,
                new MessageHeaderDeserializer()));
        MessageHeader messageHeader = mapper.readValue(message, MessageHeader.class);
        switch (messageHeader.getSource()) {
            case SERVER:
                logger.error("Server has received its own message: \n{} ", message);
                break;
            case WEBPAGE:
                processWebpageMessage(message, messageHeader);
                break;
            case ENGINE:
                processEngineMessage(message, messageHeader);
                break;
            default:
                logger.error("Unknown message source");
        }
    }

    private void processWebpageMessage(String message, MessageHeader messageHeader) {

        if (messageHeader.getType().equals(WebpageMessage.connectionACK)) {
            sendHandshakeComplete(messageHeader);
        }
        else if (messageHeader.getTargetId() != null) {
            forwardMessage(message, messageHeader);
        }
    }

    private void sendHandshakeComplete(MessageHeader messageHeader) {
        MessageHeader handshakeComplete = new MessageHeader();
        handshakeComplete.setSource(MessageSource.SERVER);
        handshakeComplete.setType(ServerMessage.handshakeComplete);
        handshakeComplete.setOriginId(messageHeader.getOriginId());
        handshakeComplete.setTargetId(messageHeader.getTargetId());
        String message = JsonConverter.convertToJsonString(handshakeComplete);
        WebsocketSession targetSession = sessions.get(handshakeComplete.getTargetId());
        sendToSession(targetSession, message);
    }

    private void processEngineMessage(String message, MessageHeader messageHeader) {
        if (messageHeader.getType().equals(EngineMessage.connectToWebpage)) {
            connectWebpageToEngine(messageHeader);
        } else {
            forwardMessage(message, messageHeader);
        }
    }

    private void connectWebpageToEngine(MessageHeader messageHeader) {
        String webpageId = messageHeader.getTargetId();
        String engineId = messageHeader.getOriginId();
        WebsocketSession engineSession = sessions.get(engineId);
        WebsocketSession webpageSession = sessions.get(webpageId);
        engineSession.setTargetId(webpageId);
        webpageSession.setTargetId(engineId);
        IdDispatcher idDispatcher = new IdDispatcher();
        idDispatcher.sendIdToClient(webpageSession, ServerMessage.targetAssignment, engineId);

    }

    private void forwardMessage(String message, MessageHeader messageHeader) {
        sendToSession(sessions.get(messageHeader.getTargetId()), message);
    }


    public void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
