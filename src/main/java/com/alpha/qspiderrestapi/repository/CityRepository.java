package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {

	@Query(value = "Select city_name from city_icon_image", nativeQuery = true)
	List<String> findCityName();
}
