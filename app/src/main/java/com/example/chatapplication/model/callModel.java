package com.example.chatapplication.model;

public class callModel {
    private String calling;
    private String receiving;



    private String id;
    public callModel(String calling, String receiving, String id) {
        this.calling = calling;
        this.receiving = receiving;
        this.id = id;
    }

    public callModel() {
    }

    public String getCalling() {
        return calling;
    }

    public void setCalling(String calling) {
        this.calling = calling;
    }

    public String getReceiving() {
        return receiving;
    }

    public void setReceiving(String receiving) {
        this.receiving = receiving;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
