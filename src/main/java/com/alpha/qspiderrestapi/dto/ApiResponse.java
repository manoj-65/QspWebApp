package com.alpha.qspiderrestapi.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiResponse<T> {
	private int statusCode;
	private HttpStatus status;
	private T data;
}
