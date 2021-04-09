package com.kpodsiadlo.eightbitcomputer.controller;

import com.kpodsiadlo.eightbitcomputer.model.Program;
import com.kpodsiadlo.eightbitcomputer.service.ProgramService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class ProgramController {

    @Inject
    ProgramService programService;

    @GET
    @Path("/program/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Program readProgram(@PathParam("id") Integer id) {
        return programService.readProgram(id);
    }

    @POST
    @Path("/program")
    @Consumes(MediaType.APPLICATION_JSON)
    public Program createProgram(Program program) {
        return programService.createProgram(program);
    }
}


