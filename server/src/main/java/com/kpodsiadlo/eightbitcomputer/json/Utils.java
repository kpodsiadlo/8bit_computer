package com.kpodsiadlo.eightbitcomputer.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import java.io.StringReader;
import java.util.Map;

import java.util.logging.Logger;


public class Utils {
    private final static Logger logger = Logger.getLogger("Utils");


    public static JsonObject getJsonObject(String message) {
        JsonObject jsonMessage = null;
        try (JsonReader jsonReader = Json.createReader(new StringReader(message))) {
            jsonMessage = jsonReader.readObject();
        } catch (Exception e) {
            Logger.getLogger("Utils/getJsonObject").severe(e.getMessage());
        } finally {
            return jsonMessage;
        }
    }

    public static String mapToJsonString(Map<String, Integer> map) {

        logger.info("mapToJsonString");
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            builder.append('"' + entry.getKey() + '"' + ':' + '"' + entry.getValue().toString() + '"' + ',');
        }
        logger.info(builder.toString());
        return builder.toString();
    }
    public static JsonObject mapToJsonObject(Map<String, Integer> map) {

        logger.info("mapToJsonString");
        JsonObjectBuilder provider = JsonProvider.provider().createObjectBuilder();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            provider.add(entry.getKey(), entry.getValue());
        }
        logger.info(provider.toString());
        return provider.build();
    }

}
