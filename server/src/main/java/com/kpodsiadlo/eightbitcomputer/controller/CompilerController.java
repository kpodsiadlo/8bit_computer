package com.kpodsiadlo.eightbitcomputer.controller;

import com.kpodsiadlo.eightbitcomputer.model.Compiler;
import com.kpodsiadlo.eightbitcomputer.model.IncomingUserProgram;
import com.kpodsiadlo.eightbitcomputer.model.Program;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/compiler")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompilerController {

    @Inject
    Compiler compiler;

    @POST
    public Program compileProgram(IncomingUserProgram incomingProgram) {
        return compiler.compile(incomingProgram);
    }
}
