package com.example.weatherforecast;

public class Message {

    private long id;
    private long time;
    private String text;
    private String image;

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }
}
