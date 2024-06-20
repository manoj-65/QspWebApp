package com.alpha.qspiderrestapi.dao;

import com.alpha.qspiderrestapi.entity.City;

public interface CityDao {

	City save(City city);

	void updateCityBranchCount();

}
