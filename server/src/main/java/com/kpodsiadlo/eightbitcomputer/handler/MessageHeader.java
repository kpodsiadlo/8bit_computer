package com.kpodsiadlo.eightbitcomputer.handler;

import com.kpodsiadlo.eightbitcomputer.handler.messages.MessageSource;
import com.kpodsiadlo.eightbitcomputer.handler.messages.MessageType;

class MessageHeader {
    private MessageSource source;
    private MessageType type;
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

    public MessageSource getSource() {
        return source;
    }

    public void setSource(MessageSource source) {
        this.source = source;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
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
