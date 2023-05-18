package com.formation.payload;

import java.io.Serializable;

public class Reponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int status ;
	String message;
	Object data;
	
	public Reponse() {
		
	}
	public Reponse(int status, String message, Object data) {
		
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	

}
