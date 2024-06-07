package com.alpha.qspiderrestapi.util;

import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;

public class ResponseUtil {

	private static <T> ApiResponse<T> initResponse() {
		return new ApiResponse<T>();
	}

	public static <T> ResponseEntity<ApiResponse<T>> getOk(T t) {
		ApiResponse<T> structure = initResponse();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setStatus(HttpStatus.OK);
		structure.setData(t);

		return ResponseEntity.status(structure.getStatus()).body(structure);
	}

	public static <T> ResponseEntity<ApiResponse<T>> getCreated(T t) {
		ApiResponse<T> structure = initResponse();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setStatus(HttpStatus.CREATED);
		structure.setData(t);

		return ResponseEntity.status(structure.getStatus()).body(structure);
	}

	public static <T> ResponseEntity<ApiResponse<T>> getBadRequest(T t) {
		ApiResponse<T> structure = initResponse();
		structure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		structure.setStatus(HttpStatus.BAD_REQUEST);
		structure.setData(t);

		return ResponseEntity.status(structure.getStatus()).body(structure);
	}

	public static <T> ResponseEntity<ApiResponse<T>> getNotFound(T t) {
		ApiResponse<T> structure = initResponse();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setStatus(HttpStatus.NOT_FOUND);
		structure.setData(t);

		return ResponseEntity.status(structure.getStatus()).body(structure);
	}

	public static <T> ResponseEntity<ApiResponse<T>> getUnauthorized(T t) {
		ApiResponse<T> structure = initResponse();
		structure.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		structure.setStatus(HttpStatus.UNAUTHORIZED);
		structure.setData(t);

		return ResponseEntity.status(structure.getStatus()).body(structure);
	}
	
	public static <T> ResponseEntity<ApiResponse<T>> getNoContent(T t) {
		ApiResponse<T> structure = initResponse();
		structure.setStatusCode(HttpStatus.NO_CONTENT.value());
		structure.setStatus(HttpStatus.NO_CONTENT);
		structure.setData(t);

		return ResponseEntity.status(structure.getStatus()).body(structure);
	}

}
