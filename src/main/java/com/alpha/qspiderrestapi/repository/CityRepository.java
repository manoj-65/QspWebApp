package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.qspiderrestapi.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {

}
