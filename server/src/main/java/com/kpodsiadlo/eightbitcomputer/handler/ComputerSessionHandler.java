package com.kpodsiadlo.eightbitcomputer.handler;

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

        try {
            boolean clockRunning = message.getBoolean("clockRunning");
            computer.setClockRunning(clockRunning);
        } catch (NullPointerException e) {
            logJsonError("clockRunning");
        }


        List<String> integerComponents = Arrays.asList(
                "memoryAddress",
                "memoryContents",
                "instructionRegisterHigherBits",
                "instructionRegisterLowerBits",
                "microinstructionCounter",
                "programCounter",
                "registerA",
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


    public void logJsonError(String jsonValueMissing) {
        logger.warning(jsonValueMissing + " not found in incoming JSON {}");
    }

    public JsonObject getComputerModelState() {
        logger.info("getComputerState");
        Map<String, Object> computerState = new HashMap<>();
        computerState.put("SOURCE", "Server");
        computerState.put("clockRunning", computer.getClockRunning());
        computerState.put("memoryAddress", computer.getMemoryAddress());
        computerState.put("memoryContents", computer.getMemoryContents());
        computerState.put("instructionRegisterHigherBits", computer.getInstructionRegisterHigherBits());
        computerState.put("instructionRegisterLowerBits", computer.getInstructionRegisterLowerBits());
        computerState.put("microinstructionCounter", computer.getMicroinstructionCounter());
        computerState.put("programCounter", computer.getProgramCounter());
        computerState.put("registerA", computer.getRegisterA());
        computerState.put("registerB", computer.getRegisterB());
        computerState.put("output", computer.getOutput());
        computerState.put("bus", computer.getBus());

        JsonObjectBuilder provider = JsonProvider.provider().createObjectBuilder();

        computerState.forEach((key, value) -> provider.add(key, value.toString()));

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
