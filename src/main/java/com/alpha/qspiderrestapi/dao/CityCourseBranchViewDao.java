package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Map;

import com.alpha.qspiderrestapi.entity.CityCourseBranchView;

public interface CityCourseBranchViewDao {

	public List<CityCourseBranchView> fetchAll();

	public  List<Map<String, Object>> fetchAllViewByCityName();
}
