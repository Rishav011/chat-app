package com.example.fireapp.Adapter;

public class Users {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String username;
    String id;
    public Users(String username, String id) {
        this.username = username;
        this.id = id;
    }


    public Users() {
    }


}
