package com.kpodsiadlo.eightbitcomputer.service;

import com.kpodsiadlo.eightbitcomputer.dao.ProgramDao;
import com.kpodsiadlo.eightbitcomputer.model.Program;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class ProgramService {
    @Inject
    ProgramDao programDao;

    public Program createProgram(Program program){
        programDao.save(program);
        return program;
    }

    public Program readProgram(Integer id){
        return programDao.findById(id);
    }

    public Program updateProgram(Integer id, Program program) {
        Program update = programDao.update(id, program);
        return program;
    }

    public Boolean deleteProgram(Integer id){
        return programDao.remove(id);
    }


}

