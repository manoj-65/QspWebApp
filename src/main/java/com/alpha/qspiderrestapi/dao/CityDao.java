package com.alpha.qspiderrestapi.dao;

import java.util.List;

import com.alpha.qspiderrestapi.entity.City;

public interface CityDao {

	City save(City city);

	List<String> fetchCityName();

	void updateCityBranchCount();

}
