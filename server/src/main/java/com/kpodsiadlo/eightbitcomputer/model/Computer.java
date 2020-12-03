package com.kpodsiadlo.eightbitcomputer.model;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Computer {
    private Integer programCounter = 0;
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

    public void setProgramCounter(Integer programCounter) {
        this.programCounter = programCounter;
    }
}
