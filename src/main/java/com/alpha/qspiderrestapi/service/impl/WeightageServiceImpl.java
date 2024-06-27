package com.alpha.qspiderrestapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.Weightage.WeightageBuilder;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.service.WeightageService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

@Service
public class WeightageServiceImpl implements WeightageService {

	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private WeightageDao weightageDao;

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(long categoryId, WeightageDto dto) {

		Optional<Category> optCategory = categoryDao.fetchCategoryById(categoryId);
		if (optCategory.isPresent()) {
			Weightage weightage = Weightage.builder().qspiders(dto.getQspiders())
							   .jspiders(dto.getJspiders())
							   .pyspiders(dto.getPyspiders())
							   .bspiders(dto.getBspiders())
							   .category(optCategory.get())
							   .build();
			weightage = weightageDao.saveCategoryWeightage(weightage);
			return ResponseUtil.getCreated(weightage);
		}
		throw new IdNotFoundException("No category found with the id: " + categoryId);

	}

}
