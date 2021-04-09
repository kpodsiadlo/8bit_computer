package com.kpodsiadlo.eightbitcomputer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Program {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String contents;

    public Program() {

    }
    public Program(String name, List<String> contents) {
        this.name = name;
        this.contents = listToString(contents);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<String>getContents() {
        String[] split = contents.split(",");
        return List.of(split);
    }

    public void setContents(List<String> contents) {
        this.contents = listToString(contents);
    }

    private String listToString(List<String> contents) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<contents.size(); i++) {
            stringBuilder.append(contents.get(i));
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
}