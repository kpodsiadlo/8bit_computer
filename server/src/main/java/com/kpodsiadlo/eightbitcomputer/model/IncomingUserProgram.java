package com.kpodsiadlo.eightbitcomputer.model;

import java.util.ArrayList;
import java.util.List;

public class IncomingUserProgram {
    private List<String> instructions = new ArrayList<>();
    private List<Integer> values = new ArrayList<>();

    public IncomingUserProgram() {
    }

    public IncomingUserProgram(List<String> instructions, List<Integer> values) {
        this.instructions = instructions;
        this.values = values;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

}
