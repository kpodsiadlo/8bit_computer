package com.kpodsiadlo.eightbitcomputer.model;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Computer {
    private Integer programCounter;

    public Integer getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(Integer programCounter) {
        this.programCounter = programCounter;
    }
}
