package com.kpodsiadlo.eightbitcomputer.controller;
import com.kpodsiadlo.eightbitcomputer.engine.EngineStarter;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/engine")
public class EngineController {

    @GET
    @Path("/start/{uuid}")
    public String startEngine(@PathParam("uuid") String uuid){
        EngineStarter.startEngine(uuid);
        return null;
    }
}
