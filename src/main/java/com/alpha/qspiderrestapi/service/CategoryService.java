package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CategoryFormResponse;
import com.alpha.qspiderrestapi.dto.CategoryResponse;
import com.alpha.qspiderrestapi.entity.Category;

public interface CategoryService {

	ResponseEntity<ApiResponse<Category>> saveCategory(Category category);

	public ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchAllCategories();

	ResponseEntity<ApiResponse<CategoryResponse>> fetchCategoryById(long categoryId);

	ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long categoryId);

	ResponseEntity<ApiResponse<Category>> assignCoursesToCategory(long categoryId, List<Long> courseIds);

	ResponseEntity<ApiResponse<List<CategoryFormResponse>>> fetchAllCategoryAndSubCategory();

}
