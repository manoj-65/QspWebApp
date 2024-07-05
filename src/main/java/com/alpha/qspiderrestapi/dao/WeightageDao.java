package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Weightage;

public interface WeightageDao {

	Weightage saveWeightage(Weightage weightage);

	void deleteWeightage(Weightage weightage);
	
	Optional<Weightage> fetchWeightageById(long weightageId);

	List<Integer> fetchMaximumWeightage(long categoryId);
}
