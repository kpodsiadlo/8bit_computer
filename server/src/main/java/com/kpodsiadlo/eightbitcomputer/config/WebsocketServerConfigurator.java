package com.kpodsiadlo.eightbitcomputer.config;

import javax.websocket.server.ServerEndpointConfig;
import java.util.List;

public class WebsocketServerConfigurator extends ServerEndpointConfig.Configurator {

    public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
        return requested.get(0);
    }
}