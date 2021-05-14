package com.kpodsiadlo.eightbitcomputer.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;

import javax.websocket.Session;
import java.io.IOException;

public class IdDispatcher {

    public void sendIdToClient(Session session, MessageType type, String clientId) {
        IdAssignMessage idAssignMessage = new IdAssignMessage(
                MessageSource.SERVER, type, clientId
        );
        String jsonMessage = convertToJsonMessage(idAssignMessage);
        sendToSession(session, jsonMessage);
    }
    

    private String convertToJsonMessage(IdAssignMessage message) {
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

