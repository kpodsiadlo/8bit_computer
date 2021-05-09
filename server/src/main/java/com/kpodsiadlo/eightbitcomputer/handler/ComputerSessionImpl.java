package com.kpodsiadlo.eightbitcomputer.handler;

import javax.websocket.Session;

public class ComputerSessionImpl extends ForwardingSession implements ComputerSession {

    public ComputerSessionImpl(Session session) {
        super(session);
    }

    private String clientType;
    private String target;

    @Override
    public String getClientType() {
        return this.clientType;
    }

    @Override
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Override
    public String getTarget() {
        return this.getTarget();
    }

    @Override
    public void setTarget(String target) {
        this.target = target;
    }
}
