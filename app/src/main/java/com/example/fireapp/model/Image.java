package com.example.fireapp.model;

import com.google.firebase.database.Exclude;

public class Image {
    public Image() {
    }

    public Image(String key, String userId, String downloadUrl,Long timestamp) {
        this.key = key;
        this.userId = userId;
        this.downloadUrl = downloadUrl;
        this.timestamp = timestamp;
    }

    public String key;
    public String userId;
    public String downloadUrl;
    public Long timestamp;


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    @Exclude
    public Users user;

    @Exclude
    public int likes = 0;

    @Exclude
    public int comments = 0;

    @Exclude
    public boolean hasLiked = false;

    @Exclude
    public String userLike;


    public void addLike() {
        this.likes++;
    }

    public void removeLike() {
        this.likes--;
    }

    public void addComments() {
        this.comments++;
    }

}
