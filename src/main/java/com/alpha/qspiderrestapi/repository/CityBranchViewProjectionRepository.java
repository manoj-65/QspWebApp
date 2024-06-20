package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.CityBranchView;

public interface CityBranchViewProjectionRepository extends ReadOnlyRepository<CityBranchView, Long> {

	@Query(value = "SELECT * FROM city_branch_count_view", nativeQuery = true)
	List<CityBranchView> findAllCityBranchInfo();
}
