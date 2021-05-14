package com.kpodsiadlo.eightbitcomputer.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;

public class StringSerializer {

    private StringSerializer() {

    }

    public static String convertToJsonString(Object object) {
        String jsonString = null;
        ObjectMapper mapper = new JsonMapper();
        mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
