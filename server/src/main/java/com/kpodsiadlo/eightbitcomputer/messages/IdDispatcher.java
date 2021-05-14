package com.kpodsiadlo.eightbitcomputer.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpodsiadlo.eightbitcomputer.handler.MessageSender;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;

import javax.websocket.Session;

public class IdDispatcher {

    public static void sendIdToClient(Session session, MessageType type,
                                      String clientId) {
        Message idAssignMessage = MessageFactory.getIdAssignMessage(
                type, clientId
        );
        String jsonMessage = convertToJsonMessage(idAssignMessage);
        MessageSender.sendToSession(session, jsonMessage);
    }
    

    private static String convertToJsonMessage(Message message) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = null;
        try {
            jsonMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonMessage;
    }

}

