package com.example.vladimir.croappwebapi.models;

/**
 * Created by Vladimir on 3/18/2018.
 */

public class Award {

    int id, pid;
    Integer value;
    String name;

    public Award() {}

    public Award(int id, String name, Integer value, int pid)
    {
        this.id = id;
        this.name = name;
        this.value = value;
        this.pid = pid;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setPid(int pid)
    {
        this.pid = pid;
    }

    public int getPid()
    {
        return this.pid;
    }
}
