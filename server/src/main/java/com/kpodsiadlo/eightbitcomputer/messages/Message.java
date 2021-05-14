package com.kpodsiadlo.eightbitcomputer.messages;

import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;

public abstract class Message {
    protected MessageSource source;
    protected MessageType type;

    public Message() {

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
}
