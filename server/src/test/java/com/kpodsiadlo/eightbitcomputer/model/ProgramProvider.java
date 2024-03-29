package com.kpodsiadlo.eightbitcomputer.model;

import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ProgramProvider {
    public static Stream<Arguments> getCompilerTestData() {
        Program add3 = new Program();
        add3.setContents("01010011,01001111,11100000,00101111," +
                "01110110,01100010,11110000,00000000," +
                "00000000,00000000,00000000,00000000," +
                "00000000,00000000,00000000,00000000,");
        List<String> add3_instructions = Arrays.asList("ldi", "sta", "out", "add", "jc", "jmp", "hlt");
        List<Integer> add3_values = Arrays.asList(3, 15, 0, 15, 6, 2);

        Program divisor = new Program();
        divisor.setContents("00011111,11100000,00011110,00111101," +
                "01001110,00011111,00111110,10001010," +
                "01110010,01100110,00011110,11100000," +
                "11110000,00000001,01010100,01010100,");
        List<String> divisor_instructions =
                Arrays.asList("lda", "out", "lda", "sub", "sta", "lda", "sub", "jz",
                        "jc", "jmp", "lda", "out", "hlt");
        List<Integer> divisor_values =
                Arrays.asList(15, 0, 14, 13, 14, 15, 14, 10,
                        2, 6, 14, 0, 0, 1, 84, 84);

        Program allInstructions = new Program();
        allInstructions.setContents(
                "00100000,01010000,10000000,00110000," +
                "01000000,11110000,00010000,01110000," +
                "01100000,00000000,11100000,00000000," +
                "00000000,00000000,00000000,00000000,");
        List<String> allInstructionsInstructions =
                Arrays.asList("add", "ldi", "jz", "sub", "sta", "hlt", "lda", "jc", "jmp", "nop", "out", "value");
        List<Integer> allInstructionsValues =
                Arrays.asList(0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0);


        return Stream.of(
                arguments(add3_instructions, add3_values, add3),
                arguments(divisor_instructions, divisor_values, divisor),
                arguments(allInstructionsInstructions, allInstructionsValues, allInstructions));
    }
}
