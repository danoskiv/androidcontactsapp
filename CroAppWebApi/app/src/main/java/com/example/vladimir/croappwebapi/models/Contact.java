package com.example.vladimir.croappwebapi.models;

/**
 * Created by Vladimir on 3/18/2018.
 */

public class Contact {

    int id, user_id;
    int flag;
    String name, email, number;

    public Contact() {}

    public Contact(int id, String name, String email, String phone, int user_id)
    {
        this.id = id;
        this.name = name;
        this.number = phone;
        this.email = email;
        this.user_id = user_id;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNumber() {
        return this.number;
    }

    public int getUserId() {
        return this.user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.number = phone;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag() {
        this.flag = 1;
    }

    public void setFlag2(int flag) {
        this.flag = flag;
    }
}
