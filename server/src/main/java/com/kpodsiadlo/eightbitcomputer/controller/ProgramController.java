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
import java.util.List;

@Path("/program")

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProgramController {

    @GET
    @Path("/{id}")
    public Program readProgram(@PathParam("id") Integer id) {
        return programService.readProgram(id);
    }

    @Inject
    ProgramService programService;

    @GET
    public List<Program> getAll(){
        return programService.getAll();
    }

    @POST
    public Program createProgram(Program program) {
        return programService.createProgram(program);
    }
}


