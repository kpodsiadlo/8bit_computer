package com.kpodsiadlo.eightbitcomputer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Program {

    @Id
    private Integer id;
    private String contents;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public List<Integer> getContents() {
        String[] split = contents.split(",");
        List<Integer> contentsList = new ArrayList<>();
        for (int i=0; i<split.length; i++) {
            contentsList.add(Integer.parseInt(split[i]));
        }
        return contentsList;
    }

    public void setContents(List<Integer> contents) {
        String contentsString = "";
        for (int i=0; i<contents.size(); i++) {
            contentsString.concat(String.valueOf(contents.get(i)));
        }
        this.contents = contentsString;
    }
}
