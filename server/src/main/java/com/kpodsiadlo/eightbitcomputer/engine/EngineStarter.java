package com.kpodsiadlo.eightbitcomputer.engine;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

import java.io.IOException;

public class EngineStarter {

    public EngineStarter() throws InstantiationException {
        throw new InstantiationException("This class should not be instantiated");
    }

    public static void startEngine(String clientId)  {

        String engineFileDirectory = "/home/krzysztof/code/8bit_computer/engine/";
        String engineFileName = "main.py";
        String python3 = "venv/bin/python3";

        String startPython =  engineFileDirectory + python3;
        String startEngine = engineFileDirectory + engineFileName;
        String line = startPython + " " + startEngine + " " + clientId;

        CommandLine startEngineCLI = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        try {
            executor.execute(startEngineCLI);
        } catch (ExecuteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
