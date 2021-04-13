package com.kpodsiadlo.eightbitcomputer.model;

import com.google.common.base.Strings;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class Compiler {

    Map<String, String> opcodes = getOpcodes();


    public Program compile(IncomingUserProgram incomingUserProgram) {
        Program compiledProgram = new Program();
        StringBuilder programBinary = new StringBuilder();

        String instruction = "";
        Integer value;
        for (int i = 0; i < 16; i++) {
            try {
                instruction = incomingUserProgram.getInstructions().get(i);
            } catch (IndexOutOfBoundsException e) {
                instruction = "";
            }

            try {
                value = incomingUserProgram.getValues().get(i);
            } catch (IndexOutOfBoundsException e) {
                value = 0;
            }

            String machineCodeInstruction = opcodes.getOrDefault(instruction, "");
            if (machineCodeInstruction.equals("")) {
                programBinary.append(convertToStringAndPad(value, 8));
            } else {
                String entry = machineCodeInstruction +
                        convertToStringAndPad(value, 4);
                programBinary.append(entry);
            }
            programBinary.append(",");
        }
        compiledProgram.setContents(programBinary.toString());
        return compiledProgram;
    }

    private String convertToStringAndPad(Integer integer, Integer pad) {
        return Strings.padStart((Integer.toBinaryString(integer)), pad, '0');
    }


    private List<String> generateEmptyList() {
        List<String> emptyList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            emptyList.add(String.valueOf("00000000"));
        }
        return emptyList;
    }

    private static Map<String, String> getOpcodes () {
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

    public static Set<String> getInstructions() {
        return getOpcodes().keySet();
    }

}
