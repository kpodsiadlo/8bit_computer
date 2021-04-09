package com.kpodsiadlo.eightbitcomputer.dao;

import com.kpodsiadlo.eightbitcomputer.model.Program;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ProgramDao {

    @PersistenceContext
    EntityManager entityManager;

    public Program save(Program program) {
        entityManager.persist(program);
        return program;
    }

    public Program findById(Integer id) {
        return entityManager.find(Program.class, id);
    }

    public Program update(Integer id, Program program) {
        Program toEdit = entityManager.find(Program.class, id);
        toEdit.setContents(program.getContents());
        entityManager.merge(toEdit);
        return toEdit;
    }

    public Boolean remove(Integer id) {
        Program toRemove = entityManager.find(Program.class, id);
        if (toRemove != null) {
            entityManager.remove(toRemove);
            return true;
        }
        return false;
    }
}
