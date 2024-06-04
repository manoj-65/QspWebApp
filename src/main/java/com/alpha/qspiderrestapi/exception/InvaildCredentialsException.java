package com.alpha.qspiderrestapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Setter
@Getter
@AllArgsConstructor
public class InvaildCredentialsException extends RuntimeException {
	private String message;
}
