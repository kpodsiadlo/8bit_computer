package com.kpodsiadlo.eightbitcomputer.controller;

import com.kpodsiadlo.eightbitcomputer.model.Compiler;
import com.kpodsiadlo.eightbitcomputer.model.IncomingUserProgram;
import com.kpodsiadlo.eightbitcomputer.model.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Logger logger = LoggerFactory.getLogger("CompilerService");

    @Inject
    Compiler compiler;

    @POST
    public Program compileProgram(IncomingUserProgram incomingProgram) {
        logger.info("Incoming Program: {}",incomingProgram);
        return compiler.compile(incomingProgram);
    }
}
