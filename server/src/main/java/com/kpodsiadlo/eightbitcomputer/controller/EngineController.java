package com.kpodsiadlo.eightbitcomputer.controller;
import com.kpodsiadlo.eightbitcomputer.engine.EngineStarter;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/engine")
public class EngineController {

    @GET
    @Path("/start")
    public String startEngine(){
        EngineStarter.startEngine();
        return null;
    }
}
