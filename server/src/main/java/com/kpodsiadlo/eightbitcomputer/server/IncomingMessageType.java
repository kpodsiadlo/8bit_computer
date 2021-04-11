package com.kpodsiadlo.eightbitcomputer.server;

public enum IncomingMessageType {
    UPDATE,
    EXECUTE_ONE_CLOCK_CYCLE,
    RESET_ENGINE,
    RAM_UPDATE,
    PING
}
