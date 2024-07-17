package com.alpha.qspiderrestapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CategoryDashboardResponse;
import com.alpha.qspiderrestapi.dto.CategoryFormResponse;
import com.alpha.qspiderrestapi.dto.CategoryResponse;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.enums.Mode;

public interface CategoryService {

	ResponseEntity<ApiResponse<Category>> saveCategory(Category category);

	public ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchAllCategories(String domainName);

	ResponseEntity<ApiResponse<CategoryResponse>> fetchCategoryById(long categoryId,String domainName);

	ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile iconfile,MultipartFile alternativeIconfile, long categoryId);

	ResponseEntity<ApiResponse<Category>> assignCoursesToCategory(long categoryId, List<Long> courseIds);

	ResponseEntity<ApiResponse<List<CategoryFormResponse>>> fetchAllCategoryAndSubCategory();

//	ResponseEntity<ApiResponse<List<CategoryDashboardResponse>>> findSortedCategories();

	ResponseEntity<ApiResponse<Map<Mode, List<CategoryDashboardResponse>>>> findSortedCategories(String domainName);

	ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchAllOnlineCourses(String domainName);

}
