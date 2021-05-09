package com.kpodsiadlo.eightbitcomputer.handler;

import javax.websocket.Session;

public interface ComputerSession extends Session {

    public String getClientType();
    public void setClientType(String clientType);
    public String getTarget();
    public void setTarget(String Target);
}
