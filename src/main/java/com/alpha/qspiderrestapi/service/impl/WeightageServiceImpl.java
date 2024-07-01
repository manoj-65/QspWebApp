package com.alpha.qspiderrestapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.service.WeightageService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

@Service
public class WeightageServiceImpl implements WeightageService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SubCategoryDao subcategoryDao;
	
	@Autowired
	private CourseDao courseDao;

	@Autowired
	private WeightageDao weightageDao;

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(long categoryId, WeightageDto dto) {

		Optional<Category> optCategory = categoryDao.fetchCategoryById(categoryId);
		if (optCategory.isPresent()) {
			if(optCategory.get().getWeightage()==null) {
				
				Weightage weightage = Weightage.builder()
											   .qspiders(dto.getQspiders())
											   .jspiders(dto.getJspiders())
											   .pyspiders(dto.getPyspiders())
											   .bspiders(dto.getBspiders())
											   .category(optCategory.get())
											   .build();
				optCategory.get().setWeightage(weightage);
				weightage.setCategory(optCategory.get());
				weightage = weightageDao.saveCategoryWeightage(weightage);
				return ResponseUtil.getCreated(weightage);
			}else {
				throw new InvalidInfoException("The given category already has a weightage");
			}
		}
		throw new IdNotFoundException("No category found with the id: " + categoryId);

	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveSubCategoryWeightage(long categoryId, long subCategoryId,
			WeightageDto dto) {

		Category category = categoryDao.fetchCategoryById(categoryId)
				.orElseThrow(() -> new IdNotFoundException("No category found with id: " + categoryId));
		SubCategory subCategory = subcategoryDao.fetchSubCategoryById(subCategoryId)
				.orElseThrow(() -> new IdNotFoundException("No subCategory found with id: " + subCategoryId));
		if (category.getSubCategories().contains(subCategory)) {
			if(subCategory.getWeightage().stream().anyMatch(w->w.getSubCategory_categoryId()==categoryId)) {
				throw new InvalidInfoException("The given category and sub-category pair already has a weihtage");
			}
				Weightage weightage = Weightage.builder()
											   .qspiders(dto.getQspiders())
											   .jspiders(dto.getJspiders())
											   .pyspiders(dto.getPyspiders())
											   .bspiders(dto.getBspiders())
											   .subCategory(subCategory)
											   .subCategory_categoryId(categoryId)
											   .build();
						weightage = weightageDao.saveCategoryWeightage(weightage);
						return ResponseUtil.getCreated(weightage);
			
		}
			throw new InvalidInfoException("In given info, category does not contain the sub-category");
	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCourseWeightage(long categoryId, Long subCategoryId,
			long courseId, WeightageDto dto) {
		
		Category category = categoryDao.fetchCategoryById(categoryId)
				.orElseThrow(() -> new IdNotFoundException("No category found with id: " + categoryId));
		
		Course course = courseDao.fetchCourseById(courseId)
				.orElseThrow(() -> new IdNotFoundException("No Course found with id: " + courseId));
		
		if(subCategoryId!=null) {
			SubCategory subCategory = subcategoryDao.fetchSubCategoryById(subCategoryId)
					.orElseThrow(() -> new IdNotFoundException("No subCategory found with id: " + subCategoryId));
			
			if (category.getSubCategories().contains(subCategory)) {
				if(subCategory.getCourses().contains(course)) {
					if(subCategory.getWeightage().stream().anyMatch(w->w.getCourse_SubCategoryId()==subCategoryId)) {
						throw new InvalidInfoException("The given course and sub-category pair already has a weihtage");
					}
					Weightage weightage = Weightage.builder()
												   .qspiders(dto.getQspiders())
												   .jspiders(dto.getJspiders())
												   .pyspiders(dto.getPyspiders())
												   .bspiders(dto.getBspiders())
												   .course(course)
												   .course_SubCategoryId(subCategoryId)
												   .build();
					weightage = weightageDao.saveCategoryWeightage(weightage);
					return ResponseUtil.getCreated(weightage);
				}
				else {
					throw new InvalidInfoException("In given info, sub-category does not contain the course");
				}
			}else {
				throw new InvalidInfoException("In given info, category does not contain the sub-category");
			}
		}
		else {
			if(category.getCourses().contains(course)) {
				if(course.getWeightages().stream().anyMatch(w->w.getCourse_categoryId()!=null && w.getCourse_categoryId()==categoryId)) {
					throw new InvalidInfoException("In given category and course pair already has a weihtage");
				}
				Weightage weightage = Weightage.builder()
											   .qspiders(dto.getQspiders())
											   .jspiders(dto.getJspiders())
											   .pyspiders(dto.getPyspiders())
											   .bspiders(dto.getBspiders())
											   .course(course)
											   .course_categoryId(categoryId)
											   .build();
				weightage = weightageDao.saveCategoryWeightage(weightage);
				return ResponseUtil.getCreated(weightage);
			}
			else {
				throw new InvalidInfoException("In given info, category does not contain the course");
			}
		}
	}

}
