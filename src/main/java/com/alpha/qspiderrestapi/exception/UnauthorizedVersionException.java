package com.alpha.qspiderrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedVersionException extends RuntimeException {

	private String message="Given version build is not available";
	
	@Override
	public String getMessage() {
		return message;
	}
}
