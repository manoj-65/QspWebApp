package com.alpha.qspiderrestapi.repository;

import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Batch;

import jakarta.transaction.Transactional;

public interface BatchRepository extends JpaRepository<Batch, Long> {

	@Query(value = "Select b.batchId From Batch b Where b.batchId = :batchId")
	Long findByBatchId(@Param("batchId") long batchId);

	@Query(value = "SELECT * FROM batch WHERE batch_status IN ('ONGOING','UPCOMING')", nativeQuery = true)
	List<Batch> fetchAllUpcomingAndOngoingBatches();

	@Modifying
	@Transactional
	@Query(value = "Update batch SET batch_status = :toStatus WHERE batch_status = :fromStatus", nativeQuery = true)
	int updateBatchStatus(String fromStatus, String toStatus);
	
	@Transactional
	@Query(value = "SELECT create_batches(:branchType, CAST(:courseIds AS bigint[]),:startTime)", nativeQuery = true)
    void createBatches(@Param("branchType") String branchType, @Param("courseIds") Long[] courseIds,Time startTime);

}
