package com.example.myapplication.model;

public class Topic {

    private String id;
    private  String name;
    private int date;
    public Topic(String id, String name, int date){
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
