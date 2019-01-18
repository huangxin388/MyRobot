package com.example.bean;

import java.util.Date;

public class ChatMessage {
    private String content;
    private Date date;
    private Type type;

    public enum Type{
        INCOMING,OUTCOMING
    }

    public ChatMessage(String content, Date date, Type type) {
        this.content = content;
        this.date = date;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
