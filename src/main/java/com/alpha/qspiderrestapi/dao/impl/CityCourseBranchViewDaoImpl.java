package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.CityCourseBranchViewDao;
import com.alpha.qspiderrestapi.entity.CityCourseBranchView;
import com.alpha.qspiderrestapi.repository.CityCourseBranchViewRepository;

@Repository
public class CityCourseBranchViewDaoImpl implements CityCourseBranchViewDao{

	@Autowired
	private CityCourseBranchViewRepository repository;
	
	@Override
	public List<CityCourseBranchView> fetchAll() {
		return repository.findAll();
	}

}
