package com.alpha.qspiderrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.BatchRequestDto;
import com.alpha.qspiderrestapi.entity.Batch;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.BatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/batches")
public class BatchController {

	@Autowired
	private BatchService batchService;

	
	@Operation(description = "A Batch associated with a course and a branch is saved into the database", summary = "Saves a Batch")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping
	public ResponseEntity<ApiResponse<Batch>> saveBatch(@PathVariable String version, @RequestParam(required = false) Long branchId,
			@RequestParam long courseId, @RequestBody BatchRequestDto batchDto) {
		if (version.equalsIgnoreCase("V1"))
			return batchService.saveBatch(branchId, courseId, batchDto);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}
	
	@DeleteMapping
	public ResponseEntity<ApiResponse<String>> deleteBatch(@PathVariable String version, @RequestParam long batchId) {
		if (version.equalsIgnoreCase("V1"))
			return batchService.deleteBatch(batchId);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}

}