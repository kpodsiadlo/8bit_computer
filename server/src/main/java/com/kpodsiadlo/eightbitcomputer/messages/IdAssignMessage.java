package com.kpodsiadlo.eightbitcomputer.messages;

import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;
import com.kpodsiadlo.eightbitcomputer.messageType.ServerMessage;

class IdAssignMessage extends Message {
    public IdAssignMessage(){
        super();
    }

    public IdAssignMessage(MessageType type, String id){
        this.source = MessageSource.SERVER;
        this.type = type;
        this.id = id;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
