package com.alpha.qspiderrestapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.exception.DuplicateDataInsertionException;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.service.SubCategoryService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the SubCategoryService interface. Provides methods to
 * handle business logic related to SubCategories.
 */
@Service
@Slf4j
public class SubCategoryServiceImpl implements SubCategoryService {

	@Autowired
	private SubCategoryDao subCategoryDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private AWSS3ServiceImpl awss3ServiceImpl;

	/**
	 * Saves a new SubCategory under a specified Category.
	 *
	 * @param categoryId  the ID of the Category to which the SubCategory will
	 *                    belong.
	 * @param subCategory the SubCategory entity to be saved.
	 * @return a ResponseEntity containing the ApiResponse with the saved
	 *         SubCategory.
	 * @throws IdNotFoundException if the Category with the given ID is not found.
	 */
	@Override
	public ResponseEntity<ApiResponse<SubCategory>> saveSubCategory(long categoryId, SubCategory subCategory) {
		log.info("Entered the saveSubCategory method");
		try {
			Category category = categoryDao.fetchCategoryById(categoryId)
					.orElseThrow(() -> new IdNotFoundException("No Category Found with the given Id"));
			subCategory.setCategory(category);

			log.info("SubCategory saved successfully.");
			return ResponseUtil.getCreated(subCategoryDao.saveSubCategory(subCategory));
		} catch (Exception e) {
			log.error("Error occurred while saving subCategory: {}", subCategory, e.getMessage());
			throw e;
		}
	}

	/**
	 * Fetches a SubCategory by its ID.
	 *
	 * @param subCategoryId the ID of the SubCategory to fetch.
	 * @return a ResponseEntity containing the ApiResponse with the fetched
	 *         SubCategory.
	 * @throws IdNotFoundException if the SubCategory with the given ID is not
	 *                             found.
	 */
	@Override
	public ResponseEntity<ApiResponse<SubCategory>> fetchSubCategoryById(long subCategoryId) {
		log.info("Entered the fetchSubCategoryById method");
		Optional<SubCategory> optional = subCategoryDao.fetchSubCategoryById(subCategoryId);
		if (optional.isPresent()) {
			SubCategory subCategory = optional.get();
			log.info("subCategory with the id: {} fetched ", subCategoryId);
			return ResponseUtil.getOk(subCategory);
		} else {
			log.error("subCategory with id: {} not found", subCategoryId);
			throw new IdNotFoundException("No subCategory Found with the Given ID");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ApiResponse<SubCategory>> assignCoursesToSubCategory(long subCategoryId,
			List<Long> courseIds) {
		log.info("Entering assignCoursesToSubCategory with subCategoryId: {} and courseIds: {}", subCategoryId,
				courseIds);

		if (subCategoryDao.isSubCategoryPresent(subCategoryId)) {
			log.info("SubCategory with id: {} is present", subCategoryId);

			courseIds.forEach(courseId -> {
				if (courseDao.isCoursePresent(courseId)) {
					log.info("Course with id: {} is present", courseId);

					if (subCategoryDao.isCourseIdPresent(subCategoryId, courseId)) {
						log.error("Course with courseId: {} for subCategoryId: {} already present", courseId,
								subCategoryId);
						throw new DuplicateDataInsertionException("Given courseId " + courseId + " already present");
					}

					subCategoryDao.assignCourseToSubCategory(subCategoryId, courseId);
					log.info("Assigned courseId: {} to subCategoryId: {}", courseId, subCategoryId);
				} else {
					log.error("Course with id: {} not found", courseId);
					throw new IdNotFoundException("Course With the Given Id = " + courseId + " Not Found");
				}
			});

			SubCategory subCategory = subCategoryDao.fetchSubCategoryById(subCategoryId).get();
			log.info("Successfully assigned courses to subCategoryId: {}", subCategoryId);
			return ResponseUtil.getOk(subCategory);
		} else {
			log.error("SubCategory with id: {} not found", subCategoryId);
			throw new IdNotFoundException("SubCategory With the Given Id Not Found");
		}
	}

	@Override
	public ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long subCategoryId) {
		try {
			String folder = "SUBCATEGORY/";
			log.info("Fetching SubCategory with id: {}", subCategoryId);
			SubCategory subCategory = subCategoryDao.fetchSubCategoryById(subCategoryId).orElseThrow(
					() -> new IdNotFoundException("SubCategory With the Id :" + subCategoryId + " Not Found"));

			folder += subCategory.getSubCategoryTitle();
			log.info("Uploading file to folder: {}", folder);
			String iconUrl = awss3ServiceImpl.uploadFile(file, folder);
			if (!(iconUrl.isEmpty())) {
				subCategory.setSubCategoryIcon(iconUrl);
				subCategoryDao.saveSubCategory(subCategory);
				log.info("Icon uploaded successfully for subCategoryId: {}", subCategoryId);
				return ResponseUtil.getCreated("Icon Uplodaed!!");
			}
			log.error("Icon upload failed due to admin restriction for subCategoryId: {}", subCategoryId);
			throw new NullPointerException("Icon can't be Upload Due the Admin restriction");
		} catch (IdNotFoundException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error occured while uploading the file", e.getMessage());
			throw e;
		}
	}

	@Override
	public ResponseEntity<ApiResponse<String>> removeCourseFromCategory(Long subCategoryId, List<Long> courseIds) {

		if (subCategoryDao.isSubCategoryPresent(subCategoryId)) {
			courseIds.forEach(courseId -> {
				if (!courseDao.isCoursePresent(courseId))
					throw new IdNotFoundException("Course With the Given Id = " + courseId + " Not Found");

				if (!subCategoryDao.isCourseIdPresent(subCategoryId, courseId))
					throw new InvalidInfoException("Given courseId " + courseId + " not mapped to given subCategory");
			});
			subCategoryDao.removeCourseFromSubCategory(subCategoryId, courseIds);
			return ResponseUtil.getNoContent("Course with the given sub Category removed");
		} else {
			throw new IdNotFoundException("SubCategory With the Given Id Not Found");
		}

	}

	@Override
	public ResponseEntity<ApiResponse<String>> removeSubCategoryAndUnmapCourses(Long subCategoryId) {
		if (categoryDao.isCategoryPresent(subCategoryId)) {
			SubCategory subCategory = subCategoryDao.fetchSubCategoryById(subCategoryId).orElseThrow(
					() -> new IdNotFoundException("Given SubCategory " + subCategoryId + " is not present"));
			List<Course> courses = subCategory.getCourses();
			if (!courses.isEmpty()) {
				subCategoryDao.removeCourseFromSubCategory(subCategoryId,
						courses.stream().map(Course::getCourseId).collect(Collectors.toList()));
				// todo: query to delete only subcategory not course
			}
			subCategoryDao.deleteSubCategory(subCategoryId);
		}
		return ResponseUtil.getOk("SubCategory Unmapped and deleted");
	}

}
