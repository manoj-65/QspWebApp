package com.alpha.qspiderrestapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.City;

import jakarta.transaction.Transactional;

public interface CityRepository extends JpaRepository<City, Long> {

	@Query(value = "Select city_name from city_icon_image", nativeQuery = true)
	List<String> findCityName();

	@Transactional
	@Query(value = "SELECT update_branch_count()", nativeQuery = true)
	void updateCityBranchCount();
	
	Optional<City> findCityByCityName(String cityName);

}
