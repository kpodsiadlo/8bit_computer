package com.kpodsiadlo.eightbitcomputer.handler;

import com.kpodsiadlo.eightbitcomputer.json.JsonUtils;
import com.kpodsiadlo.eightbitcomputer.server.IncomingMessageType;
import com.kpodsiadlo.eightbitcomputer.service.ComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.util.Optional;

@Stateless
public class ComputerMessageHandler {
    @Inject
    ComputerService computerService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public String processMessage(String message) {
        logger.info("OnMessage");
        logger.info("Received: {}", message);
        JsonObject jsonMessage = JsonUtils.getJsonObject(message);
        IncomingMessageType messageType = getJsonMessageType(jsonMessage);

        if (messageType.equals(IncomingMessageType.UPDATE)) {
            computerService.updateComputerWithJackson(jsonMessage);
            return computerService.getComputerStateAsJson();
        } else if (messageType.equals(IncomingMessageType.ERROR)) {
            logErrorMessage(message);
        }
        return message;
    }

    private IncomingMessageType getJsonMessageType(JsonObject message) {
        if (checkForKeyInMessage(message, "type")) {
            String type = message.getString("type");
            logger.info(type);
            return IncomingMessageType.valueOf(type.toUpperCase());
        } else
            return IncomingMessageType.ERROR;
    }

    private boolean checkForKeyInMessage(JsonObject message, String key) {
        Optional<String> keyInJson = Optional.empty();
        try {
            keyInJson = Optional.of(message.getString(key));
        } catch (NullPointerException ignored) {
            logger.debug("No \"{}\" key in incoming Message", key);
        }
        return keyInJson.isPresent();
    }

    private void logErrorMessage(String message) {
        logger.error("ERROR - no message type in JSON. \nMessage: {}", message);
    }
}
