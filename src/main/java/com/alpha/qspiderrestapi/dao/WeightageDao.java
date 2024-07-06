package com.alpha.qspiderrestapi.dao;

import java.util.List;

import com.alpha.qspiderrestapi.entity.Weightage;

public interface WeightageDao {

	Weightage saveWeightage(Weightage weightage);

	List<Weightage> findSubCategoryWeightages(long categoryId);

	List<Weightage> saveAllWeightage(List<Weightage> weightages);

}
