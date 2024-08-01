package com.alpha.qspiderrestapi.exception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

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

//	@ExceptionHandler(NullPointerException.class)
//	public ResponseEntity<ApiResponse<String>> catchNullPointerException(NullPointerException exception) {
//		return ResponseUtil.getBadRequest(exception.getMessage());
//	}

	@ExceptionHandler(DuplicateDataInsertionException.class)
	public ResponseEntity<ApiResponse<String>> duplicateDataInsertionException(
			DuplicateDataInsertionException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<String>> constraintViolationException(DataIntegrityViolationException exception) {
		return ResponseUtil.getBadRequest("Constraint is being violated " + exception.getRootCause().getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}

	@ExceptionHandler(InvaildCredentialsException.class)
	public ResponseEntity<ApiResponse<String>> handleInvaildCredentialsExceptions(
			InvaildCredentialsException exception) {
		return ResponseUtil.getForbidden(exception.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolationException(
			ConstraintViolationException exception) {
		Set<ConstraintViolation<?>> errorMessage = exception.getConstraintViolations();
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> constraintViolation : errorMessage) {
			errors.add(constraintViolation.getMessageTemplate());
		}
		return ResponseUtil.getBadRequest(errors);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> ref = ex.getAllErrors();
		Map<String, String> errors = new LinkedHashMap<>();
		for (ObjectError error : ref) {
			String message = error.getDefaultMessage();
			String fieldName = ((FieldError) error).getField();
			errors.put(fieldName, message);
		}
		ApiResponse<Map<String, String>> responseStructure = new ApiResponse<Map<String, String>>();
		responseStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		responseStructure.setStatus(HttpStatus.BAD_REQUEST);
		responseStructure.setData(errors);
		return new ResponseEntity<>(responseStructure, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(InvalidPhoneNumberException.class)
	public ResponseEntity<ApiResponse<String>> handleInvalidPhoneNumberException(
			InvalidPhoneNumberException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}

	@ExceptionHandler(InvalidInfoException.class)
	public ResponseEntity<ApiResponse<String>> handleInvalidInfoException(InvalidInfoException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}

	@ExceptionHandler(InvalidOrganisationTypeException.class)
	public ResponseEntity<ApiResponse<String>> handleInvalidOrganisationTypeException(
			InvalidOrganisationTypeException exception) {
		return ResponseUtil.getBadRequest(exception.getMessage());
	}

	@ExceptionHandler(DomainMismatchException.class)
	public ResponseEntity<ApiResponse<String>> handleDomainMismatchException(DomainMismatchException exception) {
		return ResponseUtil.getInternalServerError(exception.getMessage());
	}

	@ExceptionHandler(FaqNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleFaqNotFoundException(FaqNotFoundException exception) {
		return ResponseUtil.getNotFound(exception.getMessage());
	}

}
