package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Weightage;

public interface WeightageDao {

	Weightage saveWeightage(Weightage weightage);

	List<Weightage> findSubCategoryWeightages(long categoryId);

	List<Weightage> saveAllWeightage(List<Weightage> weightages);

	void deleteWeightage(Weightage weightage);

	Optional<Weightage> fetchWeightageById(long weightageId);

	List<Integer> fetchMaximumWeightage(long categoryId);

	List<Weightage> getAllWeightages(long categoryId);
}
