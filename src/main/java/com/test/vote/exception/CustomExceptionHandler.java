package com.test.vote.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequestException(final BadRequestException ex) {
		String errorMessage =  ex.getMessage();
		return ResponseEntity.status(ex.getStatusCode()).body(errorMessage);
	}

}
