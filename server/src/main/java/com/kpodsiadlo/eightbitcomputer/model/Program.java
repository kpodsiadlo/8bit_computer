package com.kpodsiadlo.eightbitcomputer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Program {

    @Id @GeneratedValue
    private Integer id;

    private String contents;

    public Program() {

    }
    public Program(List<String> contents) {
        this.contents = listToString(contents);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public List<String>getContents() {
        String[] split = contents.split(",");
        return List.of(split);
    }

    public void setContents(List<String> contents) {
        this.contents = listToString(contents);
    }

    private String listToString(List<String> contents) {
        String contentsString = "";
        for (int i=0; i<contents.size(); i++) {
            contentsString += (contents.get(i)) + ",";
        }
        return contentsString;
    }
}