package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.SubCategory;

public interface SubCategoryService {

	ResponseEntity<ApiResponse<SubCategory>> saveSubCategory(long categoryId, SubCategory subCategory);

	ResponseEntity<ApiResponse<SubCategory>> fetchSubCategoryById(long subCategoryId);

	ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long subCategoryId);

	ResponseEntity<ApiResponse<SubCategory>> assignCoursesToSubCategory(long subCategoryId, List<Long> courseIds);

	ResponseEntity<ApiResponse<String>> removeCourseFromCategory(Long subCategoryId, List<Long> courseIds);

}
