package com.alpha.qspiderrestapi.service;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Batch;

public interface BatchService {

	ResponseEntity<ApiResponse<Batch>> saveBatch(long branchId, long courseId, Batch batch);
	
	void updateBatchStatus();

	void createBatch();

	ResponseEntity<ApiResponse<String>> deleteBatch(long batchId);
}
