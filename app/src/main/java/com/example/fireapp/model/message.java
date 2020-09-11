package com.example.fireapp.model;

public class message {
    private String sender;
    private String receiver;
    private String message;
    private long time;
    public String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public message(String sender, String receiver, String message, long time,String key) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
        this.key = key;
    }

    public message() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
