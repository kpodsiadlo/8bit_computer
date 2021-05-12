package com.kpodsiadlo.eightbitcomputer.controller;
import com.kpodsiadlo.eightbitcomputer.engine.EngineControlMessage;
import com.kpodsiadlo.eightbitcomputer.engine.EngineRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/engine")
public class EngineController {

    @GET
    @Path("/start/{clientId}")
    public String startEngine(@PathParam("clientId") String clientId){
        EngineRestClient.sendControlMessage(
                EngineControlMessage.START, clientId
        );
        return null;
    }
}
