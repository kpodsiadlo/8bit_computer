package com.kpodsiadlo.eightbitcomputer.model;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Computer {
    private Integer programCounter = 0;
    private Integer registerA = 0;
    private Boolean clockRunning = true;

    public Boolean getClockRunning() {
        return clockRunning;
    }

    public void setClockRunning(Boolean clockRunning) {
        this.clockRunning = clockRunning;
    }

    public Integer getProgramCounter() {
        return programCounter;
    }

    public Integer getRegisterA() {
        return registerA;
    }

    public void setRegisterA(Integer registerA) {
        this.registerA = registerA;
    }

    public void setProgramCounter(Integer programCounter) {
        this.programCounter = programCounter;
    }
}
