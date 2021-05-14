package com.kpodsiadlo.eightbitcomputer.messages;

import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;

public class MessageHeader extends Message implements Forwardable {

    private String originId;
    private String targetId;

    public MessageHeader(MessageSource source, MessageType type, String originId,
                         String targetId) {
        this.source = source;
        this.type = type;
        this.originId = originId;
        this.targetId = targetId;
    }

    @Override
    public String getOriginId() {
        return originId;
    }

    @Override
    public void setOriginId(String originId) {
        this.originId = originId;
    }

    @Override
    public String getTargetId() {
        return targetId;
    }

    @Override
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
