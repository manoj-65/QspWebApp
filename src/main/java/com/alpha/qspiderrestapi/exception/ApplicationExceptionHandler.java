package com.alpha.qspiderrestapi.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.util.ResponseUtil;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UnauthorizedVersionException.class)
	public ResponseEntity<ApiResponse<String>> catchUnauthorisedVersionException(
			UnauthorizedVersionException exception) {
		return ResponseUtil.getUnauthorized(exception.getMessage());
	}

	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> catchIdNotFoundException(IdNotFoundException exception) {
		return ResponseUtil.getNotFound(exception.getMessage());
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ApiResponse<String>> catchNullPointerException(NullPointerException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}

	@ExceptionHandler(DuplicateDataInsertionException.class)
	public ResponseEntity<ApiResponse<String>> duplicateDataInsertionException(
			DuplicateDataInsertionException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<String>> ConstraintViolationException(DataIntegrityViolationException exception) {
		return ResponseUtil.getBadRequest("Constraint is being violated "+exception.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}
	
	@ExceptionHandler(InvaildCredentialsException.class)
	public ResponseEntity<ApiResponse<String>> handleInvaildCredentialsExceptions(InvaildCredentialsException exception) {
		return ResponseUtil.getForbidden(exception.getMessage());
	}
	
	

}
