package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Batch;

public interface BatchDao {

	Batch saveBatch(Batch batch);

	Optional<Batch> fetchBatchById(long batchId);

	List<Batch> fetchAllBatches();

	void deleteBatch(long batchId);

	long isBatchPresent(long batchId);
}
