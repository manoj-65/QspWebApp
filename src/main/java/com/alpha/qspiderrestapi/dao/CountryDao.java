package com.alpha.qspiderrestapi.dao;

import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Country;

public interface CountryDao {

	Optional<Country> findCountryByCountryName(String countryName);

}
