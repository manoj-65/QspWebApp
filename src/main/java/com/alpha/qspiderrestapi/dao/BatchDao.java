package com.alpha.qspiderrestapi.dao;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Batch;
import com.alpha.qspiderrestapi.entity.enums.BatchStatus;

public interface BatchDao {

	Batch saveBatch(Batch batch);

	Optional<Batch> fetchBatchById(long batchId);

	List<Batch> fetchAllBatches();

	void deleteBatch(long batchId);

	boolean isBatchPresent(long batchId);
	
	List<Batch> fetchAllUpcomingAndOngoingBatches();

	List<Batch> saveAll(List<Batch> batches);

	int updateBatchStatus(BatchStatus fromStatus, BatchStatus toStatus);

	void createBatches(List<String> branchTypes, Time startTime1, Time startTime2);

	void updateToOngoing();

	void updateToBlocked();

}
