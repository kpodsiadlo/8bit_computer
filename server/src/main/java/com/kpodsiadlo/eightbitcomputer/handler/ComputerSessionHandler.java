package com.kpodsiadlo.eightbitcomputer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.kpodsiadlo.eightbitcomputer.model.Computer;
import com.kpodsiadlo.eightbitcomputer.server.IncomingMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class ComputerSessionHandler {

    private final Set<Session> sessions = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Inject
    Computer computer;

    public IncomingMessageType processJsonMessage(JsonObject message) {
        if (checkForKeyInMessage(message, "tick")) {
            return IncomingMessageType.TICK;
        } else if (checkForKeyInMessage(message, "reset")) {
            return IncomingMessageType.RESET;
        } else if (checkForKeyInMessage(message, "ramUpdate")) {
            updateComputerWithJackson(message);
            return IncomingMessageType.RAM_UPDATE;
        } else {
            updateComputerWithJackson(message);
            return IncomingMessageType.UPDATE;
        }
    }

    private boolean checkForKeyInMessage(JsonObject message, String key) {
        Optional<Boolean> keyInJson = Optional.empty();
        try {
            keyInJson = Optional.of(message.getBoolean(key));
        } catch (NullPointerException ignored) {
            logger.debug("Message is not a {}", key);
        }
        return keyInJson.isPresent();
    }

    private void updateComputerWithJackson(JsonObject message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForUpdating(computer);
        try {
            computer = objectReader.readValue(message.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public String getComputerStateAsJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = "";
        try {
            s = objectMapper.writeValueAsString(computer);
            s = "" + "{\"SOURCE\":\"server\", " + s.substring(1);
        } catch (JsonProcessingException e) {
            logJsonError("Error while parsing computer model");

        }
        return s;
    }

    public JsonArray getRamContents(){
        logger.info("getRamContents");
        List<Integer> memoryContents = computer.getMemoryContents();
        JsonObjectBuilder objectBuilder = JsonProvider.provider().createObjectBuilder();
        JsonArrayBuilder arrayBuilder = JsonProvider.provider().createArrayBuilder();
        for (int i = 0; i < memoryContents.size(); i++) {
            arrayBuilder.add(memoryContents.get(i));
        }
        return arrayBuilder.build();
    }

    public void logJsonError(String jsonValueMissing) {
        String message = MessageFormat.format("{0} not found in incoming JSON '{}'", jsonValueMissing);
        logger.error(message);
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            removeSession(session);
            logger.error(e.getMessage());
        }
    }

    public void sendToAllReceivingSessions(String message, Session transmittingSession) {
        logger.info(message);
        Set<Session> receivingSessions = new HashSet<>(sessions);
        receivingSessions.remove(transmittingSession);
        receivingSessions.forEach(session -> sendToSession(session, message));

    }
}

    /*

        public void sendToAllSessions(String message) {
        sessions.forEach(session -> sendToSession(session, message));
    }

    public String getComputerModelState() {
        logger.info("getComputerState");

        JsonObjectBuilder provider = JsonProvider.provider().createObjectBuilder();

        provider.add("SOURCE", "Server");
        provider.add("clockRunning", computer.getClockRunning());
        provider.add("memoryAddress", computer.getMemoryAddress());
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
        provider.add("memoryContents", JsonUtils.listToJsonObject(computer.getMemoryContents()));

        //DON'T CHANGE INTO AN INLINE VARIABLE!
        JsonObject build = provider.build();

        return build.toString();
    }

    private void updateComputer(JsonObject message) {
        logger.info("updateComputer");
        updateClockRunning(message);
        updateMap(message, "logic");
        updateMap(message, "flags");
        updateIntegers(message);
        updateMemoryContents(message);
    }

    private void updateClockRunning(JsonObject message) {
        try {
            boolean clockRunning = message.getBoolean("clockRunning");
            computer.setClockRunning(clockRunning);
        } catch (NullPointerException e) {
            logJsonError("clockRunning");
        }
    }

    private void updateMap(JsonObject message, String mapToExtract) {
        JsonObject mapJson = null;

        try {
            mapJson = message.getJsonObject(mapToExtract);
        } catch (Exception e) {
            e.printStackTrace();
            logJsonError(mapToExtract);
        }

        HashMap<String, Integer> retrievedComponentData = null;
        if (mapJson != null) {
            logger.info("{}: {}",mapToExtract, mapJson);
            try {
                retrievedComponentData = new ObjectMapper().readValue(mapJson.toString(), HashMap.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (retrievedComponentData != null) {
            if (mapToExtract.equals("logic")) {
                computer.setLogic(retrievedComponentData);
            }
            if (mapToExtract.equals("flags")) {
                computer.setFlags((retrievedComponentData));
            }
        }
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
            updateIntegersByReflection(message, integerComponent);
        }
    }

    private void updateIntegersByReflection(JsonObject message, String integerComponent) {
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
                logger.info("Change {}: {}",integerComponent, e);
            } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        });
    }

    private void updateMemoryContents(JsonObject message) {
        Optional<JsonArray> jsonArrayOptional = Optional.ofNullable(message.getJsonArray("memoryContents"));
        jsonArrayOptional.ifPresentOrElse(
                opt -> computer.setMemoryContents(opt.getValuesAs(JsonNumber::intValue)),
                () -> logJsonError("Memory Contents"));
    }

}
     */