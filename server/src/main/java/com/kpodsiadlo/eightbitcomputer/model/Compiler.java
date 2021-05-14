package com.kpodsiadlo.eightbitcomputer.model;

import com.google.common.base.Strings;

import javax.ejb.Stateless;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class Compiler {

    private final Map<String, String> opcodes = getOpcodes();
    private final Integer PROGRAM_LENGTH = 16;

    public static Set<String> getInstructionsFromOpcodes() {
        return getOpcodes().keySet();
    }

    public Program compile(IncomingUserProgram incomingUserProgram) {
        StringBuilder programBinary = new StringBuilder();

        for (int i = 0; i < PROGRAM_LENGTH; i++) {
            String entry = createMachineCodeEntry(incomingUserProgram, i);
            programBinary.append(entry);
            programBinary.append(",");
        }
        
        Program compiledProgram = new Program();
        compiledProgram.setContents(programBinary.toString());
        return compiledProgram;
    }

    private String createMachineCodeEntry(IncomingUserProgram incomingUserProgram, int i) {
        String instruction = getInstruction(incomingUserProgram, i);
        String instructionOpcode = opcodes.getOrDefault(instruction, "");
        Integer value = getValue(incomingUserProgram, i);
        return convertToMachineCode(instructionOpcode, value);
    }

    private String convertToMachineCode(String instructionOpcode, Integer value) {
        String entry;
        if (instructionOpcode.equals("")) {
            entry = (convertToStringAndPad(value, 8));
        } else {
            entry = instructionOpcode +
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

}
