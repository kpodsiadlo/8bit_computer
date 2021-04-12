package com.kpodsiadlo.eightbitcomputer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilerTest {
    private Compiler compiler;

    @BeforeEach
    public void setUp() throws Exception {
        compiler = new Compiler();
    }

    @Test
    @DisplayName("Correctly compiles add3 program")
    public void testCompilationOfAdd3program() {
        IncomingUserProgram incomingUserProgram = new IncomingUserProgram();
        List<String> instructions = new ArrayList<String>(
                Arrays.asList("ldi", "sta", "out", "add", "jc", "jmp", "hlt"));
        List<Integer> values = new ArrayList<Integer>(
                Arrays.asList(3, 15, 0, 15, 6, 2));
        incomingUserProgram.setInstructions(instructions);
        incomingUserProgram.setValues(values);

        Program testProgram = new Program();
        String contents = "01010011,01001111,11100000,00101111," +
                "01110110,01100010,11110000,00000000," +
                "00000000,00000000,00000000,00000000," +
                "00000000,00000000,00000000,00000000,";
        testProgram.setContents(contents);

        Program compiledProgram = compiler.compile(incomingUserProgram);

        Assertions.assertTrue(compiledProgram.equals(testProgram));
    }

}
