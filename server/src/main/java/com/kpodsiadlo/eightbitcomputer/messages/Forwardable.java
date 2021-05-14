package com.kpodsiadlo.eightbitcomputer.messages;

public interface Forwardable {
    String getOriginId();

    void setOriginId(String originId);

    String getTargetId();

    void setTargetId(String targetId);
}
