package com.kpodsiadlo.eightbitcomputer.messages;

import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;

public class MessageHeader extends Message {

    private String originId = null;
    private String targetId = null;
    public MessageHeader(){

    }

    public MessageHeader(MessageSource source, MessageType type, String originId,
                         String targetId) {
        this.source = source;
        this.type = type;
        this.originId = originId;
        this.targetId = targetId;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
