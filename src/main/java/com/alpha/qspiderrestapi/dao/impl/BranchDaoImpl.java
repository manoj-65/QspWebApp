package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.BranchDao;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.entity.CityBranchView;
import com.alpha.qspiderrestapi.repository.BranchRepository;
import com.alpha.qspiderrestapi.repository.CityBranchViewProjectionRepository;

@Repository
public class BranchDaoImpl implements BranchDao {

	@Autowired
	private BranchRepository branchRepository;

	@Autowired
	private CityBranchViewProjectionRepository cityBranchViewProjectionRepository;

	@Override
	public Branch saveBranch(Branch branch) {
		return branchRepository.save(branch);
	}

	@Override
	public Optional<Branch> fetchBranchById(long branchId) {
		return branchRepository.findById(branchId);
	}
 
	@Override
	public List<Branch> fetchAllBranches() {
		return branchRepository.findAll();
	}

	@Override
	public void deleteBranch(long branchId) {
		branchRepository.deleteById(branchId); 
	}

	@Override
	public boolean isBranchPresent(long branchId) {
		return branchRepository.findByBranchId(branchId) != null;
	}

	@Override
	public Branch findBranchWithUpcomingBatches(long branchId) {
		return branchRepository.findBranchWithUpcomingBatches(branchId);
	}

	@Override
	public List<CityBranchView> fetchAllCityBranchView(long courseId) {
		return cityBranchViewProjectionRepository.findAllCityBranchInfo(courseId);
	}

	@Override
	public List<Branch> findAll() {
		return branchRepository.findAll();
	}
	@Override
	public void updateBranchLocation(long branchId, String location) {
		branchRepository.updateBranchLocation(branchId, location);
	}

}
