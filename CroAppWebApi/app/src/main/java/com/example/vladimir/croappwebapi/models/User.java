package com.example.vladimir.croappwebapi.models;

/**
 * Created by Vladimir on 3/17/2018.
 */

public class User {

    public int id;
    public String name;
    public String username;
    public String password;
    public int status;
    public String created_at;

    public User() {}

    public User(int id, String name, String username, String password, int status, String created_at)
    {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public int getStatus() {
        return this.status;
    }

    public String getCreated() {
        return this.created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreated(String created) {
        this.created_at = created;
    }
}
