package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Branch;

import jakarta.transaction.Transactional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	@Query(value = "Select b.branchId From Branch b Where b.branchId = :branchId")
	Long findByBranchId(@Param("branchId") long branchId);

	@Query("SELECT b FROM Branch b LEFT JOIN FETCH b.batches ba WHERE b.branchId = :branchId AND ba.batchStatus = 'UPCOMING'")
	Branch findBranchWithUpcomingBatches(@Param("branchId") Long branchId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE address SET location = ?2 WHERE address_id IN ( SELECT address_id FROM branch WHERE branch_id =?1)",nativeQuery = true)
	void updateBranchLocation(long branchId,String location);

}
