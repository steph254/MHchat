package com.example.mhchat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Group {
    private String groupname;
    private String groupicon;
    private String key;
    private int position;

    public Group() {
    }
    public Group (int position){
        this.position = position;
    }
    public Group(String groupname, String groupicon) {
        this.groupname = groupname;
        this.groupicon = groupicon;

    }

    public String getGroupName() {
        return groupname;
    }
    public void setGroupName(String groupname) {
        this.groupname = groupname;
    }
    public String getGroupIcon() {
        return groupicon;
    }
    public void setGroupIcon(String groupicon) {
        this.groupicon = groupicon;
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
