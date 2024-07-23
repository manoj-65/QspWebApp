package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

	@Query(value = "select * from country where country_name = :countryName", nativeQuery = true)
	Country findCountryByCountryName(String countryName);

}
