package com.kpodsiadlo.eightbitcomputer.handler;

class IdAssignMessage extends MessageHeader {
    public IdAssignMessage(){
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
