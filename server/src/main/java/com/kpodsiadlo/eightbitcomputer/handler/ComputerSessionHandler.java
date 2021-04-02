package com.kpodsiadlo.eightbitcomputer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpodsiadlo.eightbitcomputer.json.Utils;
import com.kpodsiadlo.eightbitcomputer.model.Computer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class ComputerSessionHandler {

    private final Set<Session> sessions = new HashSet<>();
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Inject
    Computer computer;

    public void updateComputerModel(JsonObject message) {
        logger.info("updateComputer");
        updateClockRunning(message);
        updateLogic(message);
        updateIntegers(message);
    }

    private void updateLogic(JsonObject message) {
        JsonObject logicJson = null;
        try {
            logicJson = message.getJsonObject("logic");
        } catch (Exception e) {
            e.printStackTrace();
            logJsonError("logic");
        }

        HashMap<String, Integer> logic = null;
        if (logicJson != null) {
            logger.info("Logic" + logicJson.toString());
            try {
                logic = new ObjectMapper().readValue(logicJson.toString(), HashMap.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (logic != null) {
            computer.setLogic(logic);
        }
    }

    private void updateIntegers(JsonObject message) {
        List<String> integerComponents = Arrays.asList(
                "memoryAddress",
                "memoryContents",
                "instructionRegisterHigherBits",
                "instructionRegisterLowerBits",
                "microinstructionCounter",
                "programCounter",
                "registerA",
                "alu",
                "registerB",
                "output",
                "bus"
        );

        for (String integerComponent : integerComponents) {

            Method method = null;
            Optional<Integer> componentStatus = Optional.empty();

            //Get the name of setter method
            try {
                String name = "set" + integerComponent.substring(0, 1).toUpperCase() + integerComponent.substring(1);
                method = Computer.class.getMethod(name, Integer.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            // get component Status
            try {
                componentStatus = Optional.ofNullable(message.getInt(integerComponent));
            } catch (NullPointerException e) {
                logJsonError(integerComponent);
            }

            // invoke the setter
            Method finalMethod = method;
            componentStatus.ifPresent(e -> {
                try {
                    finalMethod.invoke(computer, e);
                    logger.info("Change " + integerComponent + ": " + e);
                } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            });
        }
    }

    private void updateClockRunning(JsonObject message) {
        try {
            boolean clockRunning = message.getBoolean("clockRunning");
            computer.setClockRunning(clockRunning);
        } catch (NullPointerException e) {
            logJsonError("clockRunning");
        }
    }


    public void logJsonError(String jsonValueMissing) {
        logger.warning(jsonValueMissing + " not found in incoming JSON {}");
    }

    public JsonObject getComputerModelState() {
        logger.info("getComputerState");

        JsonObjectBuilder provider = JsonProvider.provider().createObjectBuilder();

        //Map<String, Object>  computerState = new HashMap<>();
        provider.add("SOURCE", "Server");
        provider.add("clockRunning", computer.getClockRunning());
        provider.add("memoryAddress", computer.getMemoryAddress());
        provider.add("memoryContents", computer.getMemoryContents());
        provider.add("instructionRegisterHigherBits", computer.getInstructionRegisterHigherBits());
        provider.add("instructionRegisterLowerBits", computer.getInstructionRegisterLowerBits());
        provider.add("microinstructionCounter", computer.getMicroinstructionCounter());
        provider.add("programCounter", computer.getProgramCounter());
        provider.add("registerA", computer.getRegisterA());
        provider.add("registerB", computer.getRegisterB());
        provider.add("output", computer.getOutput());
        provider.add("bus", computer.getBus());
        provider.add("alu", computer.getAlu());
        provider.add("logic", Utils.mapToJsonObject(computer.getLogic()));

        //computerState.forEach((key, value) -> provider.add(key, value.toString()));

        //DON'T CHANGE INTO INLINE VARIABLE!
        JsonObject build = provider.build();

        return build;
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            removeSession(session);
            logger.severe(e.getMessage());
        }
    }

    public void sendToAllSessions(JsonObject message) {
        sessions.forEach(session -> sendToSession(session, message));
    }

    public void sendToAllReceivingSessions(JsonObject message, Session transmittingSession) {
        Set<Session> receivingSessions = new HashSet<>(sessions);
        receivingSessions.remove(transmittingSession);
        receivingSessions.forEach(session -> sendToSession(session, message));

    }
}
