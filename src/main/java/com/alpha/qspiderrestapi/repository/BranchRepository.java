package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	@Query(value = "Select b.branchId From Branch b Where b.branchId = :branchId")
	Long findByBranchId(@Param("branchId") long branchId);
	
	@Query("SELECT b FROM Branch b LEFT JOIN FETCH b.batches ba WHERE b.branchId = :branchId AND ba.batchStatus = 'UPCOMING'")
	Branch findBranchWithUpcomingBatches(@Param("branchId") Long branchId);
	
}
