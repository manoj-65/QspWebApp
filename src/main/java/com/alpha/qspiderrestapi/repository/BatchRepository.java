package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Batch;

public interface BatchRepository extends JpaRepository<Batch, Long> {

	@Query(value = "Select b.batchId From Batch b Where b.batchId = :batchId")
	Long findByBatchId(@Param("batchId") long batchId);
}
