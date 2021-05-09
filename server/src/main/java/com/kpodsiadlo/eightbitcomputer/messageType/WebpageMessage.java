package com.kpodsiadlo.eightbitcomputer.messageType;

public enum WebpageMessage implements MessageType {
    clockEnabled,
    advanceClock,
    reset,
    ramUpdate,
    connectionACK
}
