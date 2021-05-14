package com.kpodsiadlo.eightbitcomputer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

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

    public List<String>getContents() {
        String[] split = contents.split(",");
        return List.of(split);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setContents(List<String> contents) {
        this.contents = listToString(contents);
    }

    private String listToString(List<String> contents) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String content : contents) {
            stringBuilder.append(content);
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return Objects.equals(id, program.id) &&
                Objects.equals(name, program.name) &&
                Objects.equals(contents, program.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contents);
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}