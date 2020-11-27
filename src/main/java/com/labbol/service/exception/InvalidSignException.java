package com.labbol.service.exception;

public class InvalidSignException extends Exception {
	
	private static final long serialVersionUID = 3456421799237586692L;
	
	private final String sign;
	
	public InvalidSignException(String sign) {
		this.sign = sign;
	}
	
	public InvalidSignException(String sign , String message) {
		super(message);
		this.sign = sign;
	}

	public String getSign() {
		return sign;
	}
}
