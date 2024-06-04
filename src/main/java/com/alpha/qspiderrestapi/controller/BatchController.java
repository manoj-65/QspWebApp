package com.alpha.qspiderrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Batch;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.BatchService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/batches")
public class BatchController {

	@Autowired
	private BatchService batchService;

	@PostMapping
	public ResponseEntity<ApiResponse<Batch>> saveBatch(@PathVariable String version, @RequestParam long branchId,
			@RequestParam long courseId, @RequestBody Batch batch) {
		if (version.equalsIgnoreCase("V1"))
			return batchService.saveBatch(branchId, courseId, batch);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}

}
