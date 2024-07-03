package com.alpha.qspiderrestapi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.repository.WeightageRepository;

@Repository
public class WeightageDaoImpl implements WeightageDao {

	@Autowired
	private WeightageRepository weightageRepository;

	@Override
	public Weightage saveWeightage(Weightage weightage) {
		return weightageRepository.save(weightage);
	}

	@Override
	public void deleteWeightage(long weightageId) {
		weightageRepository.deleteById(weightageId);
	}

}
