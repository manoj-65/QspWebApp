package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.City;

public interface CityDao {

	City save(City city);

	List<String> fetchCityName();

	void updateCityBranchCount();

	Optional<City> findCityByCityName(String cityName);

}
