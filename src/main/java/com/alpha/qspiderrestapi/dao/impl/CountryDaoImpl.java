package com.alpha.qspiderrestapi.dao.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.CountryDao;
import com.alpha.qspiderrestapi.entity.Country;
import com.alpha.qspiderrestapi.repository.CountryRepository;

@Repository
public class CountryDaoImpl implements CountryDao {

	@Autowired
	private CountryRepository countryRepository;

	@Override
	public Optional<Country> findCountryByCountryName(String countryName) {
		return Optional.of(countryRepository.findCountryByCountryName(countryName));
	}

}
