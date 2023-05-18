package com.formation.payload;

import java.io.Serializable;

public class ConnectData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String login;
	String password;
	
	

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
