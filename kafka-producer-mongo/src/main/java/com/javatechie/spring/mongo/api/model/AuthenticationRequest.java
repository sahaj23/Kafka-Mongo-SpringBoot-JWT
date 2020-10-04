package com.javatechie.spring.mongo.api.model;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable{

	private String userName,password;

	public AuthenticationRequest() {
		super();
	}

	public AuthenticationRequest(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
