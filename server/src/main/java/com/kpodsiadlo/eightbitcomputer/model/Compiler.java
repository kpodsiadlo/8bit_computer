package com.kpodsiadlo.eightbitcomputer.model;

import com.google.common.base.Strings;

import javax.ejb.Stateless;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class Compiler {

    Map<String, String> opcodes = getOpcodes();

    private static Map<String, String> getOpcodes() {
        return Stream.of(new String[][]
                {
                        {"nop", "0000"},
                        {"lda", "0001"},
                        {"add", "0010"},
                        {"sub", "0011"},
                        {"sta", "0100"},
                        {"ldi", "0101"},
                        {"jmp", "0110"},
                        {"jc", "0111"},
                        {"jz", "1000"},
                        {"out", "1110"},
                        {"hlt", "1111"},
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    }

    public static Set<String> getInstructionsFromOpcodes() {
        return getOpcodes().keySet();
    }

    public Program compile(IncomingUserProgram incomingUserProgram) {
        Program compiledProgram = new Program();
        StringBuilder programBinary = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            String entry = createMachineCodeEntry(incomingUserProgram, i);
            programBinary.append(entry);
            programBinary.append(",");
        }

        compiledProgram.setContents(programBinary.toString());
        return compiledProgram;
    }

    private String createMachineCodeEntry(IncomingUserProgram incomingUserProgram, int i) {
        String instruction = getInstruction(incomingUserProgram, i);
        String machineCodeInstruction = opcodes.getOrDefault(instruction, "");
        Integer value = getValue(incomingUserProgram, i);
        return createEntry(machineCodeInstruction, value);
    }

    private String createEntry(String machineCodeInstruction, Integer value) {
        String entry;
        if (machineCodeInstruction.equals("")) {
            entry = (convertToStringAndPad(value, 8));
        } else {
            entry = machineCodeInstruction +
                    convertToStringAndPad(value, 4);
        }
        return entry;
    }

    private Integer getValue(IncomingUserProgram incomingUserProgram, int i) {
        Integer value;
        try {
            value = incomingUserProgram.getValues().get(i);
        } catch (IndexOutOfBoundsException e) {
            value = 0;
        }
        return value;
    }

    private String getInstruction(IncomingUserProgram incomingUserProgram, int i) {
        String instruction;
        try {
            instruction = incomingUserProgram.getInstructions().get(i);
        } catch (IndexOutOfBoundsException e) {
            instruction = "";
        }
        return instruction;
    }

    private String convertToStringAndPad(Integer integer, Integer pad) {
        return Strings.padStart((Integer.toBinaryString(integer)), pad, '0');
    }

}
