package com.alpha.qspiderrestapi.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class DuplicateDataInsertionException extends RuntimeException {

	private String message;

	@Override
	public String getMessage() {
		return message;
	}
}
