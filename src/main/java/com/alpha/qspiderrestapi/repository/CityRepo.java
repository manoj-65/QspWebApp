package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.qspiderrestapi.entity.CityCourseBranchView;


public interface CityRepo extends JpaRepository<CityCourseBranchView, Long> {


}
