package com.alpha.qspiderrestapi.dao.impl;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.BatchDao;
import com.alpha.qspiderrestapi.entity.Batch;
import com.alpha.qspiderrestapi.entity.enums.BatchStatus;
import com.alpha.qspiderrestapi.repository.BatchRepository;

@Repository
public class BatchDaoImpl implements BatchDao {

	@Autowired
	private BatchRepository batchRepository;

	@Override
	public Batch saveBatch(Batch batch) {
		return batchRepository.save(batch);
	}

	@Override
	public Optional<Batch> fetchBatchById(long batchId) {
		return batchRepository.findById(batchId);
	}

	@Override
	public List<Batch> fetchAllBatches() {
		return batchRepository.findAll();
	}

	@Override
	public void deleteBatch(long batchId) {
		batchRepository.deleteById(batchId);
	}

	@Override
	public boolean isBatchPresent(long batchId) {
		return batchRepository.findByBatchId(batchId)!=null;
	}

	@Override
	public List<Batch> fetchAllUpcomingAndOngoingBatches() {
		return batchRepository.fetchAllUpcomingAndOngoingBatches();
	}

	@Override
	public List<Batch> saveAll(List<Batch> batches) {
		return batchRepository.saveAll(batches);
	}

	@Override
	public int updateBatchStatus(BatchStatus fromStatus, BatchStatus toStatus) {
		return batchRepository.updateBatchStatus(fromStatus.toString(), toStatus.toString());
	}

	@Override
	public void createBatches(List<String> branchTypes, Time startTime1, Time startTime2) {
		batchRepository.createBatches(branchTypes.toArray(new String[0]),startTime1,startTime2);
	}

	@Override
	public void updateToOngoing() {
		 batchRepository.updateToOngoing();
	}
	
	@Override
	public void updateToBlocked() {
		 batchRepository.updateToBlocked();
	}

}
