package com.kpodsiadlo.eightbitcomputer.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;

import javax.websocket.Session;
import java.io.IOException;

public class IdDispatcher {

    public void sendIdToClient(Session session, MessageType type, String clientId) {
        AssignIdMessage assignIdMessage = getIdAssignMessage(type,clientId);
        String jsonMessage = convertToJsonMessage(assignIdMessage);
        sendToSession(session, jsonMessage);
    }


    private AssignIdMessage getIdAssignMessage(MessageType type, String clientId) {
        AssignIdMessage assignIdMessage = new AssignIdMessage();
        assignIdMessage.setSource(MessageSource.SERVER);
        assignIdMessage.setType(type);
        assignIdMessage.setId(clientId);
        return assignIdMessage;
    }

    private String convertToJsonMessage(AssignIdMessage message) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = null;
        try {
            jsonMessage = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonMessage;
    }

    public void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

