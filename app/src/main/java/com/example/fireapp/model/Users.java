package com.example.fireapp.model;
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

    public String username;
    public String id;
    String ImageUrl;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Users(String username, String id, String ImageUrl) {
        this.username = username;
        this.id = id;
        this.ImageUrl = ImageUrl;
    }


    public Users() {
    }
}