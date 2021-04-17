package com.kpodsiadlo.eightbitcomputer.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;


class CompilerTest {
    private Compiler compiler;

    private static Stream<Arguments> dataAndResults(){
        return ProgramProvider.getCompilerTestData();
    }


    @ParameterizedTest
    @DisplayName("Correctly compiles test programs")
    @MethodSource("dataAndResults")
    void shouldTestAllTheProgramsCorrectly(List<String> instructions, List<Integer> values, Program testProgram) {
        IncomingUserProgram incomingUserProgram = new IncomingUserProgram();
        incomingUserProgram.setInstructions(instructions);
        incomingUserProgram.setValues(values);
        Program compiledProgram = compiler.compile(incomingUserProgram);
        Assertions.assertEquals(testProgram, compiledProgram);
    }

    @BeforeEach
    public void setUp() {
        compiler = new Compiler();
    }
}
