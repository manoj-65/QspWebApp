package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.repository.WeightageRepository;

@Repository
public class WeightageDaoImpl implements WeightageDao{

	@Autowired
	private WeightageRepository weightageRepository;
	
	@Override
	public Weightage saveWeightage(Weightage weightage) {
		return weightageRepository.save(weightage);
	}

	@Override
	public List<Weightage> findSubCategoryWeightages(long categoryId) {
		return weightageRepository.findBySubCategoryCategoryId(categoryId);
	}

	@Override
	public List<Weightage> saveAllWeightage(List<Weightage> weightages) {
		return weightageRepository.saveAll(weightages);
	}
	
}
