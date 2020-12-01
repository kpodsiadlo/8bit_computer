package com.kpodsiadlo.eightbitcomputer.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.logging.Logger;

public class Utils {

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
}
