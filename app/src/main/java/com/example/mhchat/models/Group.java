package com.example.mhchat.models;


import com.google.firebase.database.Exclude;

public class Group extends Room{
    public String groupname;
    public String imageURL;
    public ListMember listMember;
    private String key;
    private int position;

    public Group(){
        listMember = new ListMember();
    }

    public Group (int position){
        this.position = position;
    }
    public Group(String username, String imageUrl) {
        this.groupname = groupname;
        this.imageURL = imageUrl;
    }

    public String getGroupName() {
        return groupname;
    }
    public void setGroupName(String username) {
        this.groupname = groupname;
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
