package com.example.chatapplication.model;

import java.io.Serializable;

public class userModel implements Serializable {
    private String id, username, imageURL, status, state;

    public userModel() {
    }

    public userModel(String id, String username, String imageURL, String status, String state) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
