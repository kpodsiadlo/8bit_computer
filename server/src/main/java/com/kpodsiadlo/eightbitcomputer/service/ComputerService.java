package com.kpodsiadlo.eightbitcomputer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.kpodsiadlo.eightbitcomputer.model.Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;

@Stateless
public class ComputerService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Inject
    Computer computer;

    public void updateComputerWithJackson(JsonObject message) {
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
            s = insertDataAtTheBeginningOfJsonString(s, "SOURCE", "\"Server\"");
            s = insertDataAtTheBeginningOfJsonString(s, "update", "true");
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing computer model");

        }
        return s;
    }

    private String insertDataAtTheBeginningOfJsonString(String jsonString, String key, String value) {
        return "\"" + key + "\":" + value + ", " + jsonString.substring(1);
    }
}
