package com.kpodsiadlo.eightbitcomputer.model;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Computer {
    private Boolean clockRunning = true;
    private Integer memoryAddress = 0;
    private Integer memoryContents = 0;
    private Integer instructionRegisterHigherBits = 0;
    private Integer instructionRegisterLowerBits = 0;
    private Integer microinstructionCounter = 0;
    private Integer programCounter = 0;
    private Integer registerA = 0;
    private Integer registerB = 0;
    private Integer output = 0;
    private Integer bus = 0;

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

    public Integer getInstructionRegisterHigherBits() {
        return instructionRegisterHigherBits;
    }

    public void setInstructionRegisterHigherBits(Integer instructionRegisterHigherBits) {
        this.instructionRegisterHigherBits = instructionRegisterHigherBits;
    }

    public Integer getInstructionRegisterLowerBits() {
        return instructionRegisterLowerBits;
    }

    public void setInstructionRegisterLowerBits(Integer instructionRegisterLowerBits) {
        this.instructionRegisterLowerBits = instructionRegisterLowerBits;
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

    public Integer getBus() {
        return bus;
    }

    public void setBus(Integer bus) {
        this.bus = bus;
    }
}
