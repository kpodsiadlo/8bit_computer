package com.kpodsiadlo.eightbitcomputer.controller;
import com.kpodsiadlo.eightbitcomputer.engine.EngineStarter;
import com.kpodsiadlo.eightbitcomputer.engine.HttpEngineStarter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/engine")
public class EngineController {

    @GET
    @Path("/start/{clientId}")
    public String startEngine(@PathParam("clientId") String clientId){
        HttpEngineStarter httpEngineStarter = new HttpEngineStarter();
        httpEngineStarter.start(clientId);
        //EngineStarter.startEngine(clientId);
        return null;
    }
}
