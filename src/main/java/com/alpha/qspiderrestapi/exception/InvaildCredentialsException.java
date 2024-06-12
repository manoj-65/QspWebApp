package com.alpha.qspiderrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class InvaildCredentialsException extends RuntimeException {
	private String message;
	
	@Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
