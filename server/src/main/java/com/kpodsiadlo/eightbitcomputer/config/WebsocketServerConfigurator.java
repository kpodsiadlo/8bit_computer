package com.kpodsiadlo.eightbitcomputer.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class WebsocketServerConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig conf,
            HandshakeRequest req,
            HandshakeResponse resp) {

        conf.getUserProperties().put("handshakereq", req);
    }

}