package com.example.vladimir.croappwebapi.models;

/**
 * Created by Vladimir on 3/18/2018.
 */

public class Question {

    int id,correct,status;
    Integer pid, qid, aid,user_id;
    String body, created_at;

    public Question(){}

    public Question(int id, Integer pid, Integer qid, Integer aid, int correct, String body, int status, String created_at, Integer user_id)
    {
        this.id = id;
        this.pid = pid;
        this.qid = qid;
        this.aid = aid;
        this.correct = correct;
        this.body = body;
        this.status = status;
        this.created_at = created_at;
        this.user_id = user_id;
    }


    public int getId() {
        return this.id;
    }

    public Integer getPid() {
        return this.pid;
    }

    public Integer getQid() {
        return this.qid;
    }

    public Integer getAid() {
        return this.aid;
    }

    public int getCorrect() {
        return this.correct;
    }

    public String getBody() {
        return this.body;
    }

    public int getStatus() {
        return this.status;
    }

    public String getCreated() {
        return this.created_at;
    }

    public Integer getUserId() {
        return this.user_id;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
