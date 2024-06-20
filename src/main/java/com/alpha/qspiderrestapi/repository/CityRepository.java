package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.City;

import jakarta.transaction.Transactional;

public interface CityRepository extends JpaRepository<City, Long> {

	@Transactional
	@Query(value = "SELECT update_branch_count()", nativeQuery = true)
	 void updateCityBranchCount();
}
