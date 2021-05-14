package com.kpodsiadlo.eightbitcomputer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;

public class MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    public static void sendToSession(String message, Session session) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error("Can't send message: {} to session: {}", message,
                    session.getId());
            logger.error(e.getMessage());
        }
    }
}
