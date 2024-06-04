package com.alpha.qspiderrestapi.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IdNotFoundException extends RuntimeException {

	private String message;
	
	@Override
	public String getMessage() {
		return message;
	}
}
