package com.kpodsiadlo.eightbitcomputer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpodsiadlo.eightbitcomputer.json.JsonUtils;
import com.kpodsiadlo.eightbitcomputer.model.Computer;
import com.kpodsiadlo.eightbitcomputer.server.IncomingMessageType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class ComputerSessionHandler {

    private final Set<Session> sessions = new HashSet<>();
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Inject
    Computer computer;

    private boolean isMessageTick(JsonObject message) {
        Optional<Boolean> tick = Optional.empty();
        try {
            tick = Optional.of(message.getBoolean("tick"));
        } catch (NullPointerException ignored) {
        }
        return tick.isPresent();
    }

    public IncomingMessageType processJsonMessage(JsonObject message) {
        if (isMessageTick(message)) {
            return IncomingMessageType.TICK;
        } else {
            updateComputer(message);
            return IncomingMessageType.UPDATE;
        }
    }

    private void updateComputer(JsonObject message) {
        logger.info("updateComputer");
        updateClockRunning(message);
        updateMap(message, "logic");
        updateMap(message, "flags");
        updateIntegers(message);
        updateMemoryContents(message);
    }

    private void updateMap(JsonObject message, String component) {
        JsonObject componentJson = null;
        try {
            componentJson = message.getJsonObject(component);
        } catch (Exception e) {
            e.printStackTrace();
            logJsonError(component);
        }

        HashMap<String, Integer> retrievedComponentData = null;
        if (componentJson != null) {
            logger.info(component + componentJson.toString());
            try {
                retrievedComponentData = new ObjectMapper().readValue(componentJson.toString(), HashMap.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (retrievedComponentData != null) {
            if (component.equals("logic")) {
                computer.setLogic(retrievedComponentData);
            }
            if (component.equals("flags")) {
                computer.setFlags((retrievedComponentData));
            }
        }
    }

    private void updateMemoryContents(JsonObject message){
        Optional<JsonArray> jsonArrayOptional = Optional.ofNullable(message.getJsonArray("memoryContents"));
        jsonArrayOptional.ifPresentOrElse(
                opt -> computer.setMemoryContents(opt.getValuesAs(JsonNumber::intValue)),
                ()-> logJsonError("Memory Contents"));
    }

    private void updateIntegers(JsonObject message) {
        List<String> integerComponents = Arrays.asList(
                "memoryAddress",
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
                componentStatus = Optional.of(message.getInt(integerComponent, 0));
            } catch (NullPointerException | ClassCastException e) {
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
        logger.warning(MessageFormat.format("{0} not found in incoming JSON '{}'", jsonValueMissing));
    }

    public JsonObject getComputerModelState() {
        logger.info("getComputerState");

        JsonObjectBuilder provider = JsonProvider.provider().createObjectBuilder();

        provider.add("SOURCE", "Server");
        provider.add("clockRunning", computer.getClockRunning());
        provider.add("memoryAddress", computer.getMemoryAddress());
        provider.add("memoryContents", JsonUtils.listToJsonObject(computer.getMemoryContents()));
        provider.add("instructionRegisterHigherBits", computer.getInstructionRegisterHigherBits());
        provider.add("instructionRegisterLowerBits", computer.getInstructionRegisterLowerBits());
        provider.add("microinstructionCounter", computer.getMicroinstructionCounter());
        provider.add("programCounter", computer.getProgramCounter());
        provider.add("registerA", computer.getRegisterA());
        provider.add("registerB", computer.getRegisterB());
        provider.add("output", computer.getOutput());
        provider.add("bus", computer.getBus());
        provider.add("alu", computer.getAlu());
        provider.add("logic", JsonUtils.mapToJsonObject(computer.getLogic()));
        provider.add("flags", JsonUtils.mapToJsonObject(computer.getFlags()));

        //DON'T CHANGE INTO AN INLINE VARIABLE!
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
