package com.example.mhchat.models;

import com.google.firebase.database.Exclude;

public class Thoughts {
    private String username;
    private String imageURL;
    private String key;
    private String thought;
    private int position;

    public Thoughts() {
    }
    public Thoughts (int position){
        this.position = position;
    }
    public Thoughts(String username, String imageUrl ,String thought) {
        if (username.trim().equals("")) {
            username = "Just Me";
        }
        this.username = username;
        this.imageURL = imageUrl;
        this.thought = thought;
    }
    public String getThought() {
        return thought;
    }
    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getUserName() {
        return username;
    }
    public void setUserName(String username) {
        this.username = username;
    }
    public String getImageUrl() {
        return imageURL;
    }
    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
