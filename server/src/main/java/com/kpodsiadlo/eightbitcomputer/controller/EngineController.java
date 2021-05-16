package com.kpodsiadlo.eightbitcomputer.controller;
import com.kpodsiadlo.eightbitcomputer.engine.EngineControlMessage;
import com.kpodsiadlo.eightbitcomputer.engine.EngineRestClient;
import com.kpodsiadlo.eightbitcomputer.handler.WebSocketsSessionHandler;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("/engine")
public class EngineController {

    @Inject
    WebSocketsSessionHandler webSocketsSessionHandler;

    @GET
    @Path("/start/{clientId}")
    public String startEngine(@PathParam("clientId") String clientId) {
        String uuid = String.valueOf(clientId);
        if (webSocketsSessionHandler.sessionExist(clientId)) {
            EngineRestClient.sendControlMessage(
                    EngineControlMessage.START, clientId
            );
            return "Engine started";
        }
        return "Engine not started";
    }
}
