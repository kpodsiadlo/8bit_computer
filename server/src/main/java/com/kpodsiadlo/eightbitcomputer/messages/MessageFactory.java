package com.kpodsiadlo.eightbitcomputer.messages;

import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;

public class MessageFactory {

    private MessageFactory() {

    }

    public static Message getIdAssignMessage(MessageType type,
                                             String id) {
        return new IdAssignMessage(type, id);
    }

    public static Message getMessageHeader(MessageSource source,
                                           MessageType type,
                                           String originId,
                                           String targetId) {
        return new MessageHeader(source, type, originId, targetId);
    }

}
