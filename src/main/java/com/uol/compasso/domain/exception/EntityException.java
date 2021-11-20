package com.uol.compasso.domain.exception;

public class EntityException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public EntityException(String message) {
		super(message);
	}

	public EntityException(String message, Throwable cause) {
		super(message, cause);		
	}
	
}