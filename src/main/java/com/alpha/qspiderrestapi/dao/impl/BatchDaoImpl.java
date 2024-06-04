package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.BatchDao;
import com.alpha.qspiderrestapi.entity.Batch;
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
	public long isBatchPresent(long batchId) {
		return batchRepository.findByBatchId(batchId);
	}

}
