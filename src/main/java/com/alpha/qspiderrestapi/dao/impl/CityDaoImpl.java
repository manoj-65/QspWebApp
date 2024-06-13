package com.alpha.qspiderrestapi.dao.impl;

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

}