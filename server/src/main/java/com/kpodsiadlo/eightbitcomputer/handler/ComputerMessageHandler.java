package com.kpodsiadlo.eightbitcomputer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kpodsiadlo.eightbitcomputer.json.JsonConverter;
import com.kpodsiadlo.eightbitcomputer.json.MessageHeaderDeserializer;
import com.kpodsiadlo.eightbitcomputer.messageType.EngineMessage;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.ServerMessage;
import com.kpodsiadlo.eightbitcomputer.messageType.WebpageMessage;
import com.kpodsiadlo.eightbitcomputer.messages.IdDispatcher;
import com.kpodsiadlo.eightbitcomputer.messages.Message;
import com.kpodsiadlo.eightbitcomputer.messages.MessageFactory;
import com.kpodsiadlo.eightbitcomputer.messages.MessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.MessageHandler;
import java.util.Map;


public class ComputerMessageHandler implements MessageHandler.Whole<String> {
    private final Map<String, WebsocketSession> sessions;
    private final Logger logger = LoggerFactory.getLogger(ComputerMessageHandler.class);

    public ComputerMessageHandler(Map<String, WebsocketSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void onMessage(String message) {
        processMessage(message);
    }

    public void processMessage(String message) {
        MessageHeader messageHeader = getMessageHeader(message);
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

    private MessageHeader getMessageHeader(String message) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                false);
        mapper.registerModule(new SimpleModule().addDeserializer(MessageHeader.class,
                new MessageHeaderDeserializer()));
        MessageHeader messageHeader = null;
        try {
            messageHeader = mapper.readValue(message, MessageHeader.class);
        } catch (JsonProcessingException e) {
            logger.error("Invalid message format: \n{}", message);
        }
        return messageHeader;
    }

    private void processWebpageMessage(String message, MessageHeader messageHeader) {

        if (messageHeader.getType().equals(WebpageMessage.connectionACK)) {
            sendHandshakeComplete(messageHeader);
        } else if (messageHeader.getTargetId() != null) {
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
        MessageSender.sendToSession(targetSession, message);
    }

    private void processEngineMessage(String message, MessageHeader messageHeader) {
        if (messageHeader.getType().equals(EngineMessage.connectToWebpage)) {
            connectWebpageToEngine(messageHeader);
        } else {
            forwardMessage(message, messageHeader);
        }
    }

    private void connectWebpageToEngine(MessageHeader messageHeader) {
        String engineId = messageHeader.getOriginId();
        String webpageId = messageHeader.getTargetId();

        WebsocketSession engineSession = sessions.get(engineId);
        engineSession.setSource(MessageSource.ENGINE);
        engineSession.setTargetId(webpageId);

        WebsocketSession webpageSession = sessions.get(webpageId);
        webpageSession.setSource(MessageSource.WEBPAGE);
        webpageSession.setTargetId(engineId);

        Message idAssignMessage = MessageFactory.getIdAssignMessage(ServerMessage.targetAssignment, engineId);
        MessageSender.sendToSession(webpageSession,
                JsonConverter.convertToJsonString(idAssignMessage));

//        IdDispatcher.sendIdToClient(webpageSession, ServerMessage.targetAssignment,
//                engineId);

    }


    private void forwardMessage(String message, MessageHeader messageHeader) {
        try {
            MessageSender.sendToSession(sessions.get(messageHeader.getTargetId()), message);
        } catch (NullPointerException e) {
            logger.error("Session {} does not exist", messageHeader.getTargetId());
        }
    }


}
