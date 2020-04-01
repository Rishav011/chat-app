package com.example.fireapp.model;

import com.google.firebase.database.Exclude;

public class Comment {
    public String id;
    public String comment;
    public long timestamp;
    @Exclude
    public Users user;

    public Comment() {
    }

    public Comment(String id, String comment,long timestamp) {
        this.id = id;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
