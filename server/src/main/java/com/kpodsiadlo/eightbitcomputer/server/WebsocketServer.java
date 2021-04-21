package com.kpodsiadlo.eightbitcomputer.server;

import com.kpodsiadlo.eightbitcomputer.config.WebsocketServerConfigurator;
import com.kpodsiadlo.eightbitcomputer.engine.EngineStarter;
import com.kpodsiadlo.eightbitcomputer.handler.WebSocketsSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint(value = "/computer", configurator =  WebsocketServerConfigurator.class)
public class WebsocketServer {
    @Inject
    private WebSocketsSessionHandler sessionHandler;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("OnOpen");
        logger.info("Subprotocol: " + session.getNegotiatedSubprotocol());
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
        logger.error("onError");
        error.printStackTrace();
    }

}
