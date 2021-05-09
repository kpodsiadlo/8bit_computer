package com.kpodsiadlo.eightbitcomputer.messages;

class AssignIdMessage extends MessageHeader {
    public AssignIdMessage(){
        super();
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
