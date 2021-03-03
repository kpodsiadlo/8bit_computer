package com.kpodsiadlo.eightbitcomputer.model;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Computer {
    private Boolean clockRunning = true;
    private Integer memoryAddress = 0;
    private Integer memoryContents = 0;
    private Integer instructionRegister = 0;
    private Integer microinstructionCounter = 0;
    private Integer programCounter = 0;
    private Integer registerA = 0;
    private Integer registerB = 0;
    private Integer output = 0;

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

    public Integer getRegisterA() {
        return registerA;
    }

    public void setRegisterA(Integer registerA) {
        this.registerA = registerA;
    }

    public Integer getMemoryAddress() {
        return memoryAddress;
    }

    public void setMemoryAddress(Integer memoryAddress) {
        this.memoryAddress = memoryAddress;
    }

    public Integer getMemoryContents() {
        return memoryContents;
    }

    public void setMemoryContents(Integer memoryContents) {
        this.memoryContents = memoryContents;
    }

    public Integer getInstructionRegister() {
        return instructionRegister;
    }

    public void setInstructionRegister(Integer instructionRegister) {
        this.instructionRegister = instructionRegister;
    }

    public Integer getMicroinstructionCounter() {
        return microinstructionCounter;
    }

    public void setMicroinstructionCounter(Integer microinstructionCounter) {
        this.microinstructionCounter = microinstructionCounter;
    }

    public Integer getRegisterB() {
        return registerB;
    }

    public void setRegisterB(Integer registerB) {
        this.registerB = registerB;
    }

    public Integer getOutput() {
        return output;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }
}
