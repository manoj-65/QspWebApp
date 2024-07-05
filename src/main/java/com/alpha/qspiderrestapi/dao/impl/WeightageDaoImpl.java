package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

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
	public void deleteWeightage(Weightage weightage) {
		weightageRepository.delete(weightage);
	}

	@Override
	public Optional<Weightage> fetchWeightageById(long weightageId) {
		return weightageRepository.findById(weightageId);
	}

	@Override
	public List<Integer> fetchMaximumWeightage(long categoryId) {
		return weightageRepository.fetchWeightById(categoryId);
	}
	
	

}
