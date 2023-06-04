package com.test.vote.exception;

public class BadRequestException extends RuntimeException {

	private final int statusCode;

	public BadRequestException(final String message) {
		super(message);
		this.statusCode = 400;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
