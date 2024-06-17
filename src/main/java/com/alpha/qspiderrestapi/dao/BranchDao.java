package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.dto.BranchCourseDto;
import com.alpha.qspiderrestapi.entity.Branch;

public interface BranchDao {

	Branch saveBranch(Branch branch);

	Optional<Branch> fetchBranchById(long branchId);

	List<Branch> fetchAllBranches();

	void deleteBranch(long branchId);

	long isBranchPresent(long branchId);
	
	List<BranchCourseDto> fetchAllBranchDto();
}
