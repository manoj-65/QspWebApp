package com.alpha.qspiderrestapi.service;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.enums.Organization;

public interface WeightageService {

	ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(long categoryId, WeightageDto dto);

	ResponseEntity<ApiResponse<Weightage>> saveSubCategoryWeightage(long categoryId, long subCategoryId,
			WeightageDto dto);

	ResponseEntity<ApiResponse<Weightage>> saveCourseWeightage(long categoryId, Long subCategoryId, long courseId,
			WeightageDto dto);

	ResponseEntity<ApiResponse<Weightage>> saveCityWeightage(String cityName, WeightageDto dto);

	ResponseEntity<ApiResponse<String>> updateSubCategoryWeightage(long categoryId, long subCategoryId,
			Organization organization, long weightage);

	ResponseEntity<ApiResponse<String>> deleteCategoryWeightage(Long categoryId);

	ResponseEntity<ApiResponse<String>> deleteSubCategoryWeightage(Long subCategoryId);

	ResponseEntity<ApiResponse<String>> deleteCourseWeightage(Long courseId);

	ResponseEntity<ApiResponse<String>> updateCategoryWeightage(Long categoryId, Long weightage,
			Organization organization);

	ResponseEntity<ApiResponse<String>> updateCourseWeightage(long categoryId, Long subCategoryId, long courseId,
			WeightageDto weightageDto);

	ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightageAndIncrement(long categoryId, WeightageDto dto);

	ResponseEntity<ApiResponse<Weightage>> saveCountryWeightage(String countryName, WeightageDto dto);

	ResponseEntity<ApiResponse<String>> updateCourseWeightageForAdminForm(long categoryId, Long subCategoryId,
			long courseId, Long weightage, Organization organisation);

}
