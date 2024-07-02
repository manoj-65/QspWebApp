package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.ViewAllHomePageDao;
import com.alpha.qspiderrestapi.entity.ViewAllHomePage;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.repository.ViewAllHomePageRepository;

@Service
public class ViewAllHomePageDaoImpl implements ViewAllHomePageDao {

	@Autowired
	ViewAllHomePageRepository repository;

	@Override
	public List<ViewAllHomePage> fetchAllViewByCityName(Organization organisation) {
		return repository.findByOrganizationType(organisation.toString());
	}
}
