package com.canimesh.revolut.assignment.exception;

import lombok.Getter;

@Getter
public class ApplicationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int status = 500;
	private String message = "Internal server error";
	public ApplicationRuntimeException(int status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}
	
	public ApplicationRuntimeException(String message) {
		super(message);
		this.message = message;
	}

}
