package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CategoryFormResponse;
import com.alpha.qspiderrestapi.dto.CategoryResponse;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.exception.DuplicateDataInsertionException;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.modelmapper.CategoryMapper;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.CategoryService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private AWSS3Service awss3Service;

	/**
	 * Saves a new category.
	 *
	 * This method overrides the default behavior of `saveCategory` and persists the
	 * provided category entity to the database.
	 * 
	 * @param category The category data to be saved.
	 * @return A ResponseEntity object containing the saved category information and
	 *         the HTTP status code. The status code will be: * 201 Created - if the
	 *         category is saved successfully. * 400 Bad Request - if the category
	 *         data is invalid or missing required fields. * 500 Internal Server
	 *         Error - if an unexpected error occurs during saving.
	 * @throws Exception (or a more specific exception type) If an error occurs
	 *                   during data access.
	 */
	@Override
	public ResponseEntity<ApiResponse<Category>> saveCategory(Category category) {
		log.info("Saving category: {}", category);
		return ResponseUtil.getCreated(categoryDao.saveCategory(category));
	}

	/**
	 * Retrieves all available categories.
	 *
	 * This method overrides the default behavior of `fetchAllCategories` and
	 * fetches all category entities from the database. It then converts each entity
	 * to a CategoryResponse DTO using the `CategoryMapper.mapToCategoryDto` method.
	 * 
	 * @return A ResponseEntity object containing a list of CategoryResponse DTOs
	 *         and the HTTP status code. The status code will be: * 200 OK - if
	 *         categories are retrieved successfully. * 500 Internal Server Error -
	 *         if an unexpected error occurs during retrieval.
	 * @throws Exception (or a more specific exception type) If an error occurs
	 *                   during data access.
	 */
	@Override
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchAllCategories() {

		log.info("Entering fetchAllCategories");
		List<Category> categories = categoryDao.fetchAllCategories();
		List<CategoryResponse> categoryResponse = new ArrayList<CategoryResponse>();
		categories.forEach(category -> categoryResponse.add(CategoryMapper.mapToCategoryDto(category)));
		log.info("Category list has been upadated and set");
		return ResponseUtil.getOk(categoryResponse);
	}

	// fetches category based on the given id
	@Override
	public ResponseEntity<ApiResponse<CategoryResponse>> fetchCategoryById(long categoryId) {
		Category category = categoryDao.fetchCategoryById(categoryId).orElseThrow(() -> {
			log.error("Category not found with ID: {}", categoryId);
			return new IdNotFoundException();
		});
		log.info("Category fetched successfully: {}", category);
		return ResponseUtil.getOk(CategoryMapper.mapToCategoryDto(category));

	}

	@Override
	public ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long categoryId) {
		log.info("Uploading icon for category ID: {}", categoryId);
		String folder = "CATEGORY/";
		Optional<Category> optionalcategory = categoryDao.fetchCategoryById(categoryId);
		if (optionalcategory.isPresent()) {
			folder += optionalcategory.get().getCategoryTitle();
			String iconUrl;
			try {
				iconUrl = awss3Service.uploadFile(file, folder);
				log.info("File Uploaded successfully to sw3: {}", iconUrl);
			} catch (NullPointerException e) {
				log.error("Error uploading icon to S3: {}", e.getMessage());
				throw new NullPointerException("Error uploading icon");
			}
			optionalcategory.get().setCategoryIcon(iconUrl);
			categoryDao.saveCategory(optionalcategory.get());
			return ResponseUtil.getCreated("Icon Uploaded!!");
		}
		log.error("Failed to fetch category with the given Id: {}", categoryId);
		throw new IdNotFoundException("Category With the Given Id Not Found");

	}

	@Override
	@Transactional
	public ResponseEntity<ApiResponse<Category>> assignCoursesToCategory(long categoryId, List<Long> courseIds) {
		log.info("Assigning courses to category ID: {}", categoryId);
		if (categoryDao.isCategoryPresent(categoryId)) {
			courseIds.stream().forEach(id -> {
				if (courseDao.isCoursePresent(id)) {
					if (categoryDao.isCourseIdPresent(categoryId, id)) {
						log.warn("Duplicate course ID: {} already assigned to category", id);
						throw new DuplicateDataInsertionException("Given courseId: " + id + " already present");
					}
				} else {
					log.error("Course not found with ID: {}", id);
					throw new IdNotFoundException("Course With the Given Id: " + id + " Not Found");
				}
				categoryDao.assignCourseToCategory(categoryId, id);
			});
			return ResponseUtil.getOk(categoryDao.fetchCategoryById(categoryId).get());
		} else
			log.error("Category not found with ID: {}", categoryId);
		throw new IdNotFoundException("Category With the Given Id Not Found");
	}

	@Override
	public ResponseEntity<ApiResponse<List<CategoryFormResponse>>> fetchAllCategoryAndSubCategory() {

		List<Category> categoryList = categoryDao.fetchAllCategories();
		log.info("Fetching all the category and subcategory list from dao");
		List<CategoryFormResponse> categoryFormResponse = new ArrayList<CategoryFormResponse>();
		log.info("Fetched {} categories from DAO", categoryList.size());
		categoryList.stream().forEach((category) -> {
			categoryFormResponse.add(CategoryMapper.mapToCategoryFormResponse(category));
			log.debug("Mapped category ID {} to CategoryFormResponse", category.getCategoryId());
		});
		return ResponseUtil.getOk(categoryFormResponse);
	}
}
