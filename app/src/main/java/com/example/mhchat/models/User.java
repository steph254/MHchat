package com.example.mhchat.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
	public String username;
	public String email;
	public String avatar;

	public User() {
	}

	public User(String username, String email, String avatar) {
		this.username = username;
		this.email = email;
		this.avatar = avatar;
	}
}