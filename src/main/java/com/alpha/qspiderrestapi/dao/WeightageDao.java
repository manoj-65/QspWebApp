package com.alpha.qspiderrestapi.dao;

import com.alpha.qspiderrestapi.entity.Weightage;

public interface WeightageDao {

	Weightage saveWeightage(Weightage weightage);

	void deleteWeightage(long weightageId);

}
