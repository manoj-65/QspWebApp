package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CategoryDashboardResponse;
import com.alpha.qspiderrestapi.dto.CategoryFormResponse;
import com.alpha.qspiderrestapi.dto.CategoryRequestDto;
import com.alpha.qspiderrestapi.dto.CategoryResponse;
import com.alpha.qspiderrestapi.dto.CourseResponse;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.DuplicateDataInsertionException;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.exception.InvalidOrganisationTypeException;
import com.alpha.qspiderrestapi.modelmapper.CategoryMapper;
import com.alpha.qspiderrestapi.modelmapper.CourseMapper;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.CategoryService;
import com.alpha.qspiderrestapi.util.CategoryUtil;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.WeightageUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SubCategoryDao subCategoryDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private AWSS3Service awss3Service;

	@Autowired
	private CategoryUtil categoryUtil;

	@Autowired
	private WeightageUtil weightageUtil;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private WeightageDao weightageDao;

	@Value("${organization.qsp}")
	private String qspDomainName;

	@Value("${organization.jsp}")
	private String jspDomainName;

	@Value("${organization.pysp}")
	private String pyspDomainName;

	@Value("${organization.bsp}")
	private String prospDomainName;

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
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchAllCategories(String domainName, boolean isOnline,
			Organization organization) {

		List<Category> categories = categoryDao.fetchAllCategories();
		categories = weightageUtil.getSortedCategory(categories, domainName);
		List<CategoryResponse> categoryResponse = new ArrayList<CategoryResponse>();
		if (!Objects.isNull(organization)) {
			String domainNameKey = getDomainName(organization);
			categories.forEach(category -> categoryResponse
					.add(categoryMapper.mapToCategoryDto(category, domainNameKey, isOnline)));
		} else {
			categories.forEach(
					category -> categoryResponse.add(categoryMapper.mapToCategoryDto(category, domainName, isOnline)));
		}

		log.info("Category list has been upadated and set");
		return ResponseUtil.getOk(categoryResponse);
	}

	private String getDomainName(Organization organization) {
		switch (organization) {
		case QSP: {
			return qspDomainName;
		}
		case JSP: {
			return jspDomainName;
		}
		case PYSP: {
			return pyspDomainName;
		}
		case PROSP: {
			return prospDomainName;
		}
		default: {
			throw new InvalidOrganisationTypeException("Given Organization " + organization + " type not found");
		}

		}
	}

	// fetches category based on the given id
	@Override
	public ResponseEntity<ApiResponse<CategoryResponse>> fetchCategoryById(long categoryId, String domainName) {
		boolean isOnline = false;
		Category category = categoryDao.fetchCategoryById(categoryId).orElseThrow(() -> {
			log.error("Category not found with ID: {}", categoryId);
			return new IdNotFoundException();
		});
		log.info("Category fetched successfully: {}", category);
		return ResponseUtil.getOk(categoryMapper.mapToCategoryDto(category, domainName, isOnline));

	}

	@Override
	public ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile iconfile, MultipartFile alternativeIconfile,
			long categoryId) {
		log.info("Uploading icon for category ID: {}", categoryId);
		String folder = "CATEGORY/";
		Optional<Category> optionalcategory = categoryDao.fetchCategoryById(categoryId);
		if (optionalcategory.isPresent()) {
			folder += optionalcategory.get().getCategoryTitle();
			String iconUrl;
			String alternativeIconUrl;
			try {
				iconUrl = awss3Service.uploadFile(iconfile, folder);
				alternativeIconUrl = awss3Service.uploadFile(alternativeIconfile, folder);
				log.info("icon file Uploaded successfully to sw3: {}", iconUrl);
				log.info("alternative Icon file Uploaded successfully to sw3: {}", alternativeIconUrl);
			} catch (NullPointerException e) {
				log.error("Error uploading icon to S3: {}", e.getMessage());
				throw new NullPointerException("Error uploading icon");
			}
			optionalcategory.get().setCategoryIcon(iconUrl);
			optionalcategory.get().setCategoryAlternativeIcon(alternativeIconUrl);
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
			categoryFormResponse.add(categoryMapper.mapToCategoryFormResponse(category));
			log.debug("Mapped category ID {} to CategoryFormResponse", category.getCategoryId());
		});
		return ResponseUtil.getOk(categoryFormResponse);
	}

//	@Override
//	public ResponseEntity<ApiResponse<List<CategoryDashboardResponse>>> findSortedCategories() {
//		List<Category> categories = categoryDao.fetchAllCategories();
//
//		return ResponseUtil.getOk(categoryUtil.mapToCategoryDashboardResponse(categories));
//	}

	@Override
	public ResponseEntity<ApiResponse<Map<Mode, List<CategoryDashboardResponse>>>> findSortedCategories(
			String domainName) {
		List<Category> categories = categoryDao.fetchAllCategories();
		categories = weightageUtil.getSortedCategory(categories, domainName);
		for (Category category : categories) {
			if (!category.getSubCategories().isEmpty()) {
				List<Course> subCategoryCourses = new ArrayList<Course>();
				List<SubCategory> sortedSubCategory = weightageUtil.getSortedSubCategory(category.getSubCategories(),
						domainName, category.getCategoryId());
				for (SubCategory subCategory : sortedSubCategory) {
					List<Course> sortedCourseOfSubCategory = weightageUtil.getSortedCourseOfSubCategory(
							subCategory.getCourses(), domainName, subCategory.getSubCategoryId());
					subCategoryCourses.addAll(sortedCourseOfSubCategory);
				}
				List<Course> distinctCourses = subCategoryCourses.stream().distinct().collect(Collectors.toList());
				category.setCourses(distinctCourses);
			}
		}
		Map<Mode, List<CategoryDashboardResponse>> result = new HashMap<Mode, List<CategoryDashboardResponse>>();

		for (Mode mode : Mode.values()) {

			List<CategoryDashboardResponse> filteredCategory = categories.stream().filter(
					category -> category.getCourses().stream().anyMatch(course -> course.getMode().contains(mode)))
					.map(category -> mapToCategoryDashboardResponse(category, mode, domainName))
					.collect(Collectors.toList());

			result.put(mode, filteredCategory);
		}
		return ResponseUtil.getOk(result);
	}

	public CategoryDashboardResponse mapToCategoryDashboardResponse(Category category, Mode mode, String domainName) {
		return CategoryDashboardResponse.builder().categoryName(category.getCategoryTitle())
				.categoryId(category.getCategoryId()).categoryAlternativeIcon(category.getCategoryAlternativeIcon())
				.categoryIcon(category.getCategoryIcon())
				.courses(mapToCourse(weightageUtil.getSortedCourseOfCategory(category.getCourses(), domainName,
						category.getCategoryId()), mode, category.getCategoryId()))
				.build();
	}

	private List<CourseResponse> mapToCourse(List<Course> courses, Mode mode, long categoryId) {
		return courses.stream().filter(course -> course.getMode().contains(mode))
				.map(course -> courseMapper.mapToCourseResponse(course, categoryId)).collect(Collectors.toList());
	}

	@Override
	public ResponseEntity<ApiResponse<String>> removeCourseFromCategory(Long categoryId, List<Long> courseIds) {

		if (categoryDao.isCategoryPresent(categoryId)) {
			// validation for mapping and availability
			courseIds.stream().forEach(id -> {
				if (!courseDao.isCoursePresent(id))
					throw new IdNotFoundException("Course With the Given Id: " + id + " Not Found");
				if (!categoryDao.isCourseIdPresent(categoryId, id))
					throw new InvalidInfoException("Given courseId: " + id + " not mapped to any category");
			});
			categoryDao.removeCourseFromCategory(courseIds, categoryId);
			return ResponseUtil.getNoContent("Course with given Ids are removed");
		} else
			log.error("Category not found with ID: {}", categoryId);
		throw new IdNotFoundException("Category With the Given Id Not Found");

	}

	@Override
	public ResponseEntity<ApiResponse<Category>> saveCategoryWithIcons(CategoryRequestDto categoryDto) {
		log.info("Saving category: {}", categoryDto);
		String folder = "CATEGORY/";

		folder += categoryDto.getTitle();
		String iconUrl;
		String alternativeIconUrl;
		if (categoryDto.getIcon() != null && categoryDto.getAlternativeIcon() != null) {
			try {
				iconUrl = awss3Service.uploadFile(categoryDto.getIcon(), folder);
				alternativeIconUrl = awss3Service.uploadFile(categoryDto.getAlternativeIcon(), folder);
				log.info("icon file Uploaded successfully to sw3: {}", iconUrl);
				log.info("alternative Icon file Uploaded successfully to sw3: {}", alternativeIconUrl);
			} catch (NullPointerException e) {
				log.error("Error uploading icon to S3: {}", e.getMessage());
				throw new NullPointerException("Error uploading icon");
			}
		} else {
			throw new NullPointerException("Sent file is null");
		}

		Category category = Category.builder().categoryTitle(categoryDto.getTitle()).categoryIcon(iconUrl)
				.categoryAlternativeIcon(alternativeIconUrl).build();

		List<Weightage> allCategoryWeightages = weightageDao.getAllWeightages();
		Weightage weightage = weightageUtil.setMaxWeightage(allCategoryWeightages);
		weightage.setCategory(category);
		category.setWeightage(weightage);

		return ResponseUtil.getCreated(categoryDao.saveCategory(category));
	}

	@Override
	public ResponseEntity<ApiResponse<Category>> editCategory(CategoryRequestDto categoryDto) {

		long categoryId = categoryDto.getId();
		MultipartFile iconfile = categoryDto.getIcon();
		MultipartFile alternativeIconfile = categoryDto.getAlternativeIcon();

		String folder = "CATEGORY/";
		Optional<Category> optionalcategory = categoryDao.fetchCategoryById(categoryId);
		if (optionalcategory.isPresent()) {
			Category category = optionalcategory.get();
			category.setCategoryTitle(categoryDto.getTitle());
			folder += optionalcategory.get().getCategoryTitle();
			String iconUrl;
			String alternativeIconUrl;
			try {

				if (iconfile != null) {
					iconUrl = awss3Service.uploadFile(iconfile, folder);
					category.setCategoryIcon(iconUrl);
				}
				if (alternativeIconfile != null) {
					alternativeIconUrl = awss3Service.uploadFile(alternativeIconfile, folder);
					category.setCategoryAlternativeIcon(alternativeIconUrl);
				}

			} catch (NullPointerException e) {
				log.error("Error uploading icon to S3: {}", e.getMessage());
				throw new NullPointerException("Error uploading icon");
			}
			return ResponseUtil.getCreated(categoryDao.saveCategory(category));
		}
		throw new IdNotFoundException("Category With the Given Id : " + categoryDto.getId() + " Not Found");

	}

	@Override
	public ResponseEntity<ApiResponse<String>> removeCategoryAndUnmapCourses(Long categoryId) {

		if (categoryDao.isCategoryPresent(categoryId)) {
			Category category = categoryDao.fetchCategoryById(categoryId)
					.orElseThrow(() -> new IdNotFoundException("Given category " + categoryId + " is not present"));
			List<SubCategory> subCategories = category.getSubCategories();
			if (!subCategories.isEmpty()) {
				subCategories.stream().forEach(subCategory -> subCategoryDao.removeCourseFromSubCategory(
						subCategory.getSubCategoryId(),
						subCategory.getCourses().stream().map(Course::getCourseId).collect(Collectors.toList())));
				// todo: query to delete only subcategory not course
				subCategoryDao.deleteAllSubCategory();
			}

			List<Course> courses = category.getCourses();
			if (!courses.isEmpty()) {
				courses.stream().forEach(course -> categoryDao.removeCourseFromCategory(
						courses.stream().map(Course::getCourseId).collect(Collectors.toList()), categoryId));

				// Had to write a query to make it delete without deleting the courses in
				// relation

			}
			categoryDao.saveCategory(category);
			categoryDao.deleteCategory(categoryId);
			return ResponseUtil.getOk("Category Unmapped and deleted");

		}
		throw new IdNotFoundException("Category Id: " + categoryId + " Not found");
	}

//	@Override
//	public ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchAllOnlineCourses(String domainName) {
//		List<CategoryResponse> categories = fetchAllCategories(domainName).getBody().getData();
//
//		List<CategoryResponse> result = categories.stream().map(categoryResponse -> {
//			List<SubCategoryResponse> subCoursesResult = categoryResponse.getSubCourse().stream().map(subCourse -> {
//				List<SubCourseResponse> subCourseResponses = subCourse.getSubCourseResponse().stream()
//						.filter(c -> c.getModes().contains(Mode.ONLINE_CLASSES)).collect(Collectors.toList());
//				subCourse.setSubCourseResponse(subCourseResponses);
//				return subCourseResponses.isEmpty() ? null : subCourse;
//			}).filter(Objects::nonNull).collect(Collectors.toList());
//
//			categoryResponse.setSubCourse(subCoursesResult);
//
//			List<CourseResponse> courseResponses = categoryResponse.getCourseResponse().stream()
//					.filter(c -> c.getModes().contains(Mode.ONLINE_CLASSES)).collect(Collectors.toList());
//			categoryResponse.setCourseResponse(courseResponses);
//
//			return (courseResponses.isEmpty() && subCoursesResult.isEmpty()) ? null : categoryResponse;
//		}).filter(Objects::nonNull).collect(Collectors.toList());
//
//		return ResponseUtil.getOk(result);
//	}

}
