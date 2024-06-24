package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.entity.CityBranchView;

public interface BranchDao {

	Branch saveBranch(Branch branch);

	Optional<Branch> fetchBranchById(long branchId);

	List<Branch> fetchAllBranches();

	void deleteBranch(long branchId);

	boolean isBranchPresent(long branchId);

	Branch findBranchWithUpcomingBatches(long branchId);

	List<CityBranchView> fetchAllCityBranchView(long courseId);

	List<Branch> findAll();
}
