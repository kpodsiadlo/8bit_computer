package com.kpodsiadlo.eightbitcomputer.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.kpodsiadlo.eightbitcomputer.messages.MessageHeader;
import com.kpodsiadlo.eightbitcomputer.messageType.EngineMessage;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageSource;
import com.kpodsiadlo.eightbitcomputer.messageType.MessageType;
import com.kpodsiadlo.eightbitcomputer.messageType.WebpageMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageHeaderDeserializer extends JsonDeserializer<MessageHeader> {
    private final Logger logger = LoggerFactory.getLogger(MessageHeaderDeserializer.class);

    @Override
    public MessageHeader deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String sourceString = node.get("source").textValue();
        MessageSource source = Enum.valueOf(MessageSource.class, sourceString);
        String typeString = node.get("type").textValue();
        MessageType type = null;
        switch (source) {
            case WEBPAGE:
                type = Enum.valueOf(WebpageMessage.class,
                        typeString);
                break;
            case ENGINE:
                type = Enum.valueOf(EngineMessage.class,
                        typeString);
                break;
            default:
                throw new IOException("Invalid source value in message");

        }

        String originId = null;
        String targetId = null;
        try{
            originId = node.get("originId").textValue();
        } catch (Exception e) {
            logger.error("No originId in message");
        }
        try {
            targetId = node.get("targetId").textValue();
        } catch (Exception e) {
            logger.error("No targetId in message");
        }

        return new MessageHeader(source, type, originId, targetId);
    }
}
