package com.example.mhchat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
	public String username;
	public String email;
	public String avatar;

	public User() {
	}

	public User(String username, String email) {
		this.username = username;
		this.email = email;
	}
	public User(String avatar){
		this.avatar = avatar;
	}

	@Exclude
	public Map<String, String> toMap(){
		HashMap<String, String> user = new HashMap<>();
		user.put("username", username);
		user.put("email", email);
		return user;
	}

}