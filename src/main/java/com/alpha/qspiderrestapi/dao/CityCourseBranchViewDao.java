package com.alpha.qspiderrestapi.dao;

import java.util.List;

import com.alpha.qspiderrestapi.entity.CityCourseBranchView;
import com.alpha.qspiderrestapi.entity.ViewAllHomePage;
import com.alpha.qspiderrestapi.entity.enums.Organization;

public interface CityCourseBranchViewDao {

	public List<CityCourseBranchView> fetchAll();

}
