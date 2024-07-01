package com.alpha.qspiderrestapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
public class DomainMismatchException extends RuntimeException {
	private String message;
}
