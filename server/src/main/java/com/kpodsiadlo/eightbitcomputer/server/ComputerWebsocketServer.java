package com.kpodsiadlo.eightbitcomputer.server;

import com.kpodsiadlo.eightbitcomputer.handler.ComputerSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/computer")
public class ComputerWebsocketServer {
    @Inject
    private ComputerSessionHandler sessionHandler;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @OnOpen
    public void onOpen(Session session) {
        logger.debug("OnOpen");
        sessionHandler.addSession(session);
    }

    @OnClose
    public void onClose(Session session) {
        logger.debug("OnClose");
        if (session != null) {
            sessionHandler.removeSession(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session originSession) {
        logger.debug("onMessage:");
        logger.debug("Received: {}", message);
        sessionHandler.forwardMessage(message, originSession);
    }

    @OnError
    public void onError(Throwable error) {
        logger.error("On Error: {}", error.getMessage());
    }

}
