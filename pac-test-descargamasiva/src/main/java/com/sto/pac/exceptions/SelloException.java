package com.sto.pac.exceptions;

public class SelloException extends Exception{
	String message;
	
	public SelloException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
