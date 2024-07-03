package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.CityDao;
import com.alpha.qspiderrestapi.entity.City;
import com.alpha.qspiderrestapi.repository.CityRepository;

@Repository
public class CityDaoImpl implements CityDao {

	@Autowired
	private CityRepository cityRepository;

	@Override
	public City save(City city) {
		return cityRepository.save(city);
	}
	
	@Override
	public void updateCityBranchCount() {
		cityRepository.updateCityBranchCount();
	}

	@Override
	public List<String> fetchCityName() {
		return cityRepository.findCityName();
	}
	
	@Override
	public Optional<City> findCityByCityName(String cityName) {
		return cityRepository.findCityByCityName(cityName);
	}

}
