
package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dao.BranchDao;
import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.dao.SubjectDao;
import com.alpha.qspiderrestapi.dao.ViewAllHomePageDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CategoryFormResponseDto;
import com.alpha.qspiderrestapi.dto.CourseFormResponseDto;
import com.alpha.qspiderrestapi.dto.CourseIdResponse;
import com.alpha.qspiderrestapi.dto.CourseRequestDto;
import com.alpha.qspiderrestapi.dto.CourseRequestImageDto;
import com.alpha.qspiderrestapi.dto.SubCategoryFormResponseDto;
import com.alpha.qspiderrestapi.dto.UpdateCourseDto;
import com.alpha.qspiderrestapi.dto.UpdateCourseRequestDto;
import com.alpha.qspiderrestapi.entity.CityBranchView;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.exception.DuplicateDataInsertionException;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.modelmapper.CourseMapper;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.CourseService;
import com.alpha.qspiderrestapi.util.ChapterUtil;
import com.alpha.qspiderrestapi.util.CourseUtil;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.ViewHomePageUtil;
import com.alpha.qspiderrestapi.util.WeightageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SubCategoryDao subCategoryDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private SubjectDao subjectDao;

	@Autowired
	private BranchDao branchDao;

	@Autowired
	private ChapterUtil chapterUtil;

	@Autowired
	private AWSS3Service awss3Service;

//	@Autowired
//	private CityDao cityDao;

	@Autowired
	private ViewAllHomePageDao viewDao;

	@Autowired
	private ViewHomePageUtil util;

	@Autowired
	private CourseUtil courseUtil;

	@Autowired
	private WeightageUtil weightageUtil;

	@Autowired
	private CourseMapper courseMapper;

	/**
	 * Saves a new course associated with a specified category (implementation).
	 *
	 * This method overrides the default behavior of `saveCoureByCategory` and saves
	 * a new course entity to the database, associating it with the provided
	 * category ID.
	 * 
	 * @param course The course data to be saved.
	 * @return A ResponseEntity object containing the saved course information and
	 *         the HTTP status code. The status code will be: * 201 Created - if the
	 *         course is saved successfully. * 500 Internal Server Error - if an
	 *         unexpected error occurs during saving.
	 * @throws Exception (or a more specific exception type) If an error occurs
	 *                   during data access.
	 */
	@Override
	@Transactional
	public ResponseEntity<ApiResponse<Course>> saveCourse(long categoryId, Long subCategoryId, Course course) {
		log.info("Entering saveCourse with categoryId: {}, subCategoryId: {}, course: {}", categoryId, subCategoryId,
				course);

		try {
			if (categoryDao.isCategoryPresent(categoryId)) {
				log.info("Category with id: {} is present", categoryId);

				if (subCategoryId != null) {
					if (subCategoryDao.isSubCategoryPresent(subCategoryId)) {
						log.info("SubCategory with id: {} is present", subCategoryId);

						course = setCourseIntoFaq(course);
						course = saveCourse(course);

						subCategoryDao.assignCourseToSubCategory(subCategoryId, course.getCourseId());
						log.info("Assigned course with id: {} to subCategory with id: {}", course.getCourseId(),
								subCategoryId);

						return ResponseUtil.getCreated(course);
					} else {
						log.error("No SubCategory found with given Id: {}", subCategoryId);
						throw new IdNotFoundException("No SubCategory found with given Id: " + subCategoryId);
					}
				} else {
					course = setCourseIntoFaq(course);
					course = saveCourse(course);

					categoryDao.assignCourseToCategory(categoryId, course.getCourseId());
					log.info("Assigned course with id: {} to category with id: {}", course.getCourseId(), categoryId);

					return ResponseUtil.getCreated(course);
				}
			} else {
				log.error("No Category found with given Id: {}", categoryId);
				throw new IdNotFoundException("No Category found with given Id: " + categoryId);
			}
		} catch (IdNotFoundException e) {
			log.error("Error in saveCourse: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred in saveCourse", e);
			throw new RuntimeException("Unexpected error occurred", e);
		}
	}

	private Course setCourseIntoFaq(Course course) {
		List<Faq> faqs = course.getFaqs().stream().peek(faq -> {
			faq.setOrganizationType(course.getBranchType().get(0));
			faq.setCourse(course);
		}).toList();
		course.setFaqs(faqs);
		return course;
	}

	private Course saveCourse(Course course) {
		if (course.getSubjects() != null && !course.getSubjects().isEmpty()) {
			for (Subject subject : course.getSubjects()) {
				subject.getCourses().add(course);
				subject = chapterUtil.mapSubjectToChapters(subject);
			}
		}
		return courseDao.saveCourse(course);
	}

	/**
	 * Retrieves all available courses (implementation).
	 *
	 * This method overrides the default behavior of `fetchAllCourse` and fetches
	 * all course entities from the database.
	 * 
	 * @return A ResponseEntity object containing a list of Course objects and the
	 *         HTTP status code. The status code will be: * 200 OK - if courses are
	 *         retrieved successfully. * 500 Internal Server Error - if an
	 *         unexpected error occurs during retrieval.
	 * @throws Exception (or a more specific exception type) If an error occurs
	 *                   during data access.
	 */
	@Override
	public ResponseEntity<ApiResponse<List<CourseFormResponseDto>>> fetchAllCourse() {
		List<CourseFormResponseDto> response = courseDao.fetchAllCourses().stream().map(course -> {
			CourseFormResponseDto courseDto = new CourseFormResponseDto();
			courseDto.setCourse_id(course.getCourseId());
			courseDto.setCourse_icon(course.getCourseIcon());
			courseDto.setCourse_name(course.getCourseName());
			courseDto.setSubjectCount(course.getSubjects().size());

			// set category list
			List<CategoryFormResponseDto> categoryResponse = course.getCategories().stream().map(category -> {
				CategoryFormResponseDto categoryDto = new CategoryFormResponseDto();
				categoryDto.setCategoryId(category.getCategoryId());
				categoryDto.setCategoryTitle(category.getCategoryTitle());
				return categoryDto;
			}).collect(Collectors.toList());
			courseDto.setCategories(categoryResponse);

			// set subCategory list
			List<SubCategoryFormResponseDto> subCategoryResponse = course.getSubCategories().stream()
					.map(subCategory -> {
						SubCategoryFormResponseDto subCategoryDto = new SubCategoryFormResponseDto();
						subCategoryDto.setSubCategoryId(subCategory.getSubCategoryId());
						subCategoryDto.setSubCategoryTitle(subCategory.getSubCategoryTitle());
						return subCategoryDto;
					}).collect(Collectors.toList());
			courseDto.setSubCategories(subCategoryResponse);

			return courseDto;
		}).sorted(Comparator.comparing(CourseFormResponseDto::getCourse_name)).collect(Collectors.toList());

		return ResponseUtil.getOk(response);
	}

	/**
	 * Retrieves a course by its ID.
	 *
	 * This method retrieves a course entity from the database based on the provided
	 * course ID.
	 * 
	 * @param courseId The ID of the course to retrieve.
	 * @return A ResponseEntity object containing the retrieved course information
	 *         and the HTTP status code. The status code will be: * 200 OK - if the
	 *         course is found successfully. * 404 Not Found - if no course is found
	 *         with the provided ID. * 500 Internal Server Error - if an unexpected
	 *         error occurs during retrieval.
	 * @throws Exception (or a more specific exception type) If an error occurs
	 *                   during data access.
	 */
	@Override
	public ResponseEntity<ApiResponse<CourseIdResponse>> fetchCourseById(long courseId) {
		log.info("Entered the fetchCourseById method");
		Optional<Course> optional = courseDao.fetchCourseById(courseId);
//		System.out.println(optional);
		if (optional.isPresent()) {
			Course course = optional.get();
			System.err.println(course.getBranchType());
			CourseIdResponse courseResponse = courseMapper.mapToCourseDto(course);
//			System.err.println(branchDao.fetchAllCityBranchView());

			List<CityBranchView> viewList = new ArrayList<CityBranchView>();
			List<CityBranchView> view = branchDao.fetchAllCityBranchView(courseId);
			Map<String, List<CityBranchView>> cityBranchViewMap = view.stream()
					.collect(Collectors.groupingBy(CityBranchView::getCity));

			for (Map.Entry<String, List<CityBranchView>> entry : cityBranchViewMap.entrySet()) {
//				String cityName = entry.getKey();
				for (CityBranchView cityBranch : view) {
					long branchCountInCity = entry.getValue().size();
					if (cityBranch.getCity().equals(entry.getKey()))
						cityBranch.setBranchCount(branchCountInCity);
					viewList.add(cityBranch);
				}
			}
			Map<String, CityBranchView> test = new HashMap<String, CityBranchView>();
			for (CityBranchView cityBranchView : viewList) {
				test.put(cityBranchView.getCity(), cityBranchView);
			}

			courseResponse.setBranches(new ArrayList<CityBranchView>(test.values()));

//			System.out.println(fetchAllCourse());
			log.info("Course with the id: {} fetched ", courseId);
			return ResponseUtil.getOk(courseResponse);
		} else {
			log.error("Course with id: {} not found", courseId);
			throw new IdNotFoundException("No Course Found with the Given ID");
		}
	}

	/**
	 * Assigns a list of subjects to a course identified by its ID (implementation).
	 *
	 * This method overrides the default behavior of `assignSubjectsToCourse` and
	 * performs the following actions in a transactional context:
	 * 
	 * 1. Verifies if the provided course ID exists. 2. Iterates through the list of
	 * subject IDs. - For each subject ID, verifies if the subject exists. - If the
	 * subject exists, maps the subject to the course using
	 * `courseDao.assignSubjectToCourse`. 3. If the course ID is not found, throws
	 * an `IdNotFoundException` with a message indicating the missing course. 4. If
	 * any subject ID is not found, throws an `IdNotFoundException` with a message
	 * indicating the missing subject ID. 5. Upon successful assignment, retrieves
	 * the updated course information using `courseDao.fetchCourseById`.
	 *
	 * @param courseId   The ID of the course to assign subjects to.
	 * @param subjectIds A list of subject IDs to be assigned to the course.
	 * @return A ResponseEntity object containing the updated course information and
	 *         the HTTP status code. The status code will be: * 200 OK - if the
	 *         subjects are successfully assigned to the course. * 404 Not Found -
	 *         if the course or any subject ID is not found.
	 * @throws Exception (or a more specific exception type) If an unexpected error
	 *                   occurs during data access.
	 */
	// Assign Subjects To Course
	@Transactional
	@Override
	public ResponseEntity<ApiResponse<Course>> assignSubjectsToCourse(long courseId, List<Long> subjectIds) {
		log.info("Assigning subjects to course. Course ID: {}, Subject IDs: {}", courseId, subjectIds);

		if (courseDao.isCoursePresent(courseId)) {
			subjectIds.stream().forEach(subjectId -> {
				if (subjectDao.isSubjectPresent(subjectId)) {
					if (courseDao.isSubjectIdPresent(courseId, subjectId)) {
						log.error("Duplicate subject ID detected: {}", subjectId);
						throw new DuplicateDataInsertionException("Given subjectId " + subjectId + " already present");
					}
				} else {
					log.error("Subject with ID {} not found", subjectId);
					throw new IdNotFoundException("Subject With the Given Id = " + subjectId + " Not Found");
				}

				log.info("Assigning subject ID {} to course ID {}", subjectId, courseId);
				courseDao.assignSubjectToCourse(courseId, subjectId);
			});

			Course course = courseDao.fetchCourseById(courseId)
					.orElseThrow(() -> new IdNotFoundException("Course With the Given Id Not Found"));
			log.info("Successfully assigned subjects to course ID: {}", courseId);
			return ResponseUtil.getOk(course);
		}

		log.error("Course with ID {} not found", courseId);
		throw new IdNotFoundException("Course With the Given Id Not Found");
	}

	@Override
	public ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long courseId) {
		log.info("Uploading icon for course ID: {}", courseId);

		String folder = "COURSE/";
		Optional<Course> optionalCourse = courseDao.fetchCourseById(courseId);

		if (optionalCourse.isPresent()) {
			Course course = optionalCourse.get();
			folder += course.getCourseName();

			log.info("Uploading icon file to folder: {}", folder);
			String iconUrl = awss3Service.uploadFile(file, folder);

			if (!iconUrl.isEmpty()) {
				log.info("File uploaded successfully. URL: {}", iconUrl);
				course.setCourseIcon(iconUrl);
				courseDao.saveCourse(course);
				log.info("Course icon updated successfully for course ID: {}", courseId);
				return ResponseUtil.getCreated(iconUrl);
			}

			log.error("Failed to upload icon due to admin restriction.");
			throw new NullPointerException("Icon can't be uploaded due to admin restriction");
		}

		log.error("Course with ID {} not found", courseId);
		throw new IdNotFoundException("Course With the Given Id Not Found");
	}

	@Override
	public ResponseEntity<ApiResponse<String>> removeCourseById(long courseId) {

		Optional<Course> course = courseDao.fetchCourseById(courseId);

		if (course.isPresent()) {

			if (!(course.get().getCategories().isEmpty())) {
				courseDao.removeCourseAndCategoryById(courseId);
			}
			if (!(course.get().getSubCategories().isEmpty())) {
				courseDao.removeCourseAndSubCategoryById(courseId);
			}
			if (!course.get().getSubjects().isEmpty())
				courseDao.removeSubjectsFromCourse(courseId,
						course.get().getSubjects().stream().map(Subject::getSubjectId).collect(Collectors.toList()));
			courseDao.deleteCourse(course.get());

		} else
			throw new IdNotFoundException("Given Course Id: " + courseId + " not found");

		return ResponseUtil.getNoContent("Course Deleted");

	}

	@Override
	public ResponseEntity<ApiResponse<String>> uploadImages(MultipartFile image, MultipartFile homePageImage,
			long courseId) {
		log.info("Uploading icon for course ID: {}", courseId);

		String folder = "COURSE/";
		Optional<Course> optionalCourse = courseDao.fetchCourseById(courseId);

		if (optionalCourse.isPresent()) {
			Course course = optionalCourse.get();
			folder += "IMAGE/" + course.getCourseName();

			log.info("Uploading image file to folder: {}", folder);
			String imageUrl = awss3Service.uploadFile(image, folder);

			log.info("Uploading image file to folder: {}", folder);
			String homePageImageUrl = awss3Service.uploadFile(homePageImage, folder);

			if (!imageUrl.isEmpty()) {
				log.info("Image File uploaded successfully. URL: {}", imageUrl);
				log.info("Home Page Image File uploaded successfully. URL: {}", homePageImageUrl);
				course.setCourseImage(imageUrl);
				course.setHomePageCourseImage(homePageImageUrl);
				courseDao.saveCourse(course);
				log.info("Course image updated successfully for course ID: {}", courseId);
				log.info("Course home page image updated successfully for course ID: {}", courseId);
				return ResponseUtil.getCreated(imageUrl + "; " + homePageImageUrl);
			}

			log.error("Failed to upload icon due to admin restriction.");
			throw new NullPointerException("Icon can't be uploaded due to admin restriction");
		}

		log.error("Course with ID {} not found", courseId);
		throw new IdNotFoundException("Course With the Given Id Not Found");
	}

//	@Override
//	public ResponseEntity<ApiResponse<List<ViewAllHomePageResponse>>> fetchViewForHomepage(String hostName) {
//		Organization orgType = util.checkOrganizationType(hostName);
//		List<ViewAllHomePage> var = viewDao.fetchAllViewByCityName(orgType);
//		List<BranchDto> branchDtos = courseUtil.getTheBranchDto(var);
//		Map<String, Map<String, List<BranchDto>>> countryCityBranchListMap = branchDtos.stream()
//				.collect(Collectors.groupingBy(BranchDto::getCountry, Collectors.groupingBy(BranchDto::getCity)));
//
//		List<ViewAllHomePageResponse> response = new ArrayList<>();
//
//		String cityName;
//		String countryName;
//		for (Map.Entry<String, Map<String, List<BranchDto>>> branchTypeCountryEntry : countryCityBranchListMap
//				.entrySet()) {
//			ViewAllHomePageResponse pageResponse = new ViewAllHomePageResponse();
//			List<CityViewDto> cities = new ArrayList<CityViewDto>();
//
//			pageResponse.setCountryName(branchTypeCountryEntry.getKey());
//			for (var branchTypeCityEntry : branchTypeCountryEntry.getValue().entrySet()) {
//				CityViewDto city = new CityViewDto();
//				city.setCityName(branchTypeCityEntry.getKey());
//				List<BranchDto> branches = new ArrayList<BranchDto>();
//				for (var branchTypeEntry : branchTypeCityEntry.getValue()) {
//					branches.add(branchTypeEntry);
//					city.setQspiders(branchTypeEntry.getQspiders());
//					city.setJspiders(branchTypeEntry.getJspiders());
//					city.setPyspiders(branchTypeEntry.getPyspiders());
//					city.setProspiders(branchTypeEntry.getProspiders());
//					pageResponse.setCtQspiders(branchTypeEntry.getCtQspiders());
//					pageResponse.setCtJspiders(branchTypeEntry.getCtJspiders());
//					pageResponse.setCtPyspiders(branchTypeEntry.getCtPyspiders());
//					pageResponse.setCtProspiders(branchTypeEntry.getCtProspiders());
//				}
//				city.setBranches(branches);
//				cities.add(city);
//			}
//			pageResponse.setCity(cities);
//
//			response.add(pageResponse);
//
////			pageResponse.setCityName(branchTypeEntry.getValue());
////			pageResponse.setBranches(branchTypeEntry.getValue());
//
//		}
//
//		List<CountryDto> countries = new ArrayList<CountryDto>();
//		ArrayList<CityDto> cities = new ArrayList<CityDto>();
//		response.stream().peek(country -> {
//			CountryDto countryObject = new CountryDto();
//			countryObject.setCountryName(country.getCountryName());
//			for (CityViewDto city : country.getCity()) {
//				CityDto cityView = new CityDto();
//				cityView.setCityName(city.getCityName());
//				cityView.setQspiders(city.getQspiders());
//				cityView.setJspiders(city.getJspiders());
//				cityView.setPyspiders(city.getPyspiders());
//				cityView.setProspiders(city.getProspiders());
//				cities.add(cityView);
//			}
//			countryObject.setCtQspiders(country.getCtQspiders());
//			countryObject.setCtJspiders(country.getCtJspiders());
//			countryObject.setCtPyspiders(country.getCtPyspiders());
//			countryObject.setCtProspiders(country.getCtProspiders());
//			countries.add(countryObject);
//		}).collect(Collectors.toList());
//
//		List<CountryDto> sortedCountry = weightageUtil.getSortedCountry(countries, hostName);
//		List<CityDto> sortedCity = weightageUtil.getSortedCity(cities, hostName);
//
//		System.err.println(sortedCountry);
//		Map<String, CountryDto> countryMap = sortedCountry.stream()
//				.collect(Collectors.toMap(CountryDto::getCountryName, Function.identity()));
//
//		Map<Long, CountryDto> countryqspWeightageMap = sortedCountry.stream()
//				.collect(Collectors.toMap(CountryDto::getCtQspiders, Function.identity()));
//
//		Map<Long, CountryDto> countryjspWeightageMap = sortedCountry.stream()
//				.collect(Collectors.toMap(CountryDto::getCtJspiders, Function.identity()));
//
//		Map<Long, CountryDto> countrypyspWeightageMap = sortedCountry.stream()
//				.collect(Collectors.toMap(CountryDto::getCtPyspiders, Function.identity()));
//
//		Map<Long, CountryDto> countryprospWeightageMap = sortedCountry.stream()
//				.collect(Collectors.toMap(CountryDto::getCtProspiders, Function.identity()));
//
//		Map<String, CityDto> cityMap = sortedCity.stream()
//				.collect(Collectors.toMap(CityDto::getCityName, Function.identity()));
//
//		Map<Long, CityDto> cityqspMap = sortedCity.stream()
//				.collect(Collectors.toMap(CityDto::getQspiders, Function.identity()));
//
//		Map<Long, CityDto> cityjspMap = sortedCity.stream()
//				.collect(Collectors.toMap(CityDto::getJspiders, Function.identity()));
//
//		Map<Long, CityDto> citypyspMap = sortedCity.stream()
//				.collect(Collectors.toMap(CityDto::getPyspiders, Function.identity()));
//
//		Map<Long, CityDto> cityprospMap = sortedCity.stream()
//				.collect(Collectors.toMap(CityDto::getProspiders, Function.identity()));
//
//		System.err.println(countryMap);
//		response.stream().forEach((pageResponse) -> {
//			pageResponse.setCountryName(countryMap.get(pageResponse.getCountryName()).getCountryName());
//			pageResponse.setCtQspiders(countryqspWeightageMap.get(pageResponse.getCountryName()).getCtQspiders());
//			pageResponse.setCtJspiders(countryjspWeightageMap.get(pageResponse.getCountryName()).getCtJspiders());
//			pageResponse.setCtPyspiders(countrypyspWeightageMap.get(pageResponse.getCountryName()).getCtPyspiders());
//			pageResponse.setCtProspiders(countryprospWeightageMap.get(pageResponse.getCountryName()).getCtProspiders());
//			pageResponse.getCity().stream().forEach((cityResponse) -> {
//				cityResponse.setCityName(cityMap.get(cityResponse.getCityName()).getCityName());
//				cityResponse.setQspiders(cityqspMap.get(cityResponse.getCityName()).getQspiders());
//				cityResponse.setJspiders(cityjspMap.get(cityResponse.getCityName()).getJspiders());
//				cityResponse.setPyspiders(citypyspMap.get(cityResponse.getCityName()).getPyspiders());
//				cityResponse.setProspiders(cityprospMap.get(cityResponse.getCityName()).getProspiders());
//
//			});
//		});
//
//		// Convert BranchDto objects to Branch objects (assuming conversion logic)
////		for (BranchDto dto : branchDtos) {
////			branches.add(dto);
////		}
//
//		// Create and populate ViewAllHomePageResponse object
//
//		// Now set the list of Branch objects
//
////			Map<String, BranchDto> test = new HashMap<String, BranchDto>();
////			for (BranchDto branchDto: branches) {
////				test.put(branchDto.getCity(), branchDto);
////			}
//
////			List<ViewAllHomePageResponse> response = new ArrayList<ViewAllHomePageResponse>();
//		//
////			for (Entry<String, Map<String, List<BranchDto>>> entry : cityBranchMap.entrySet()) {
////				ViewAllHomePageResponse pageResponse = new ViewAllHomePageResponse();
////				pageResponse.setBranches(entry.getValue());
////				pageResponse.setCityName(entry.getKey());
////				response.add(pageResponse);
//		//
////			}
//
//		return ResponseUtil.getOk(response);
//	}

	@Override
	public ResponseEntity<ApiResponse<Course>> saveCourseAlongWithImages(long categoryId, Long subCategoryId,
			String courseRequest, MultipartFile icon, MultipartFile image, MultipartFile homePageImage) {

		if (categoryDao.isCategoryPresent(categoryId)) {

			if (subCategoryId != null) {
				if (subCategoryDao.isSubCategoryPresent(subCategoryId)) {

					Course course = mapAndSetUrlsToCourse(categoryId, subCategoryId, courseRequest, icon, image,
							homePageImage);

					subCategoryDao.assignCourseToSubCategory(subCategoryId, course.getCourseId());

					return ResponseUtil.getCreated(course);
				} else {
					throw new IdNotFoundException("No SubCategory found with given Id: " + subCategoryId);
				}
			} else {
				Course course = mapAndSetUrlsToCourse(categoryId, subCategoryId, courseRequest, icon, image,
						homePageImage);

				categoryDao.assignCourseToCategory(categoryId, course.getCourseId());

				return ResponseUtil.getCreated(course);
			}
		} else {
			throw new IdNotFoundException("No Category found with given Id: " + categoryId);
		}
	}

	private Course mapAndSetUrlsToCourse(long categoryId, Long subCategoryId, String courseRequest, MultipartFile icon,
			MultipartFile image, MultipartFile homePageImage) {

		// string of json body to object
		ObjectMapper objectMapper = new ObjectMapper();
		CourseRequestDto value;
		try {
			value = objectMapper.readValue(courseRequest, CourseRequestDto.class);
		} catch (JsonProcessingException e) {
			System.err.println(e.getMessage());
			throw new InvalidInfoException("The Json body format is incorrect");
		}

		// dto to course
		Course course = Course.builder().courseName(value.getCourseName())
				.courseDescription(value.getCourseDescription()).branchType(value.getBranchType()).mode(value.getMode())
				.courseAbout(value.getCourseAbout()).courseSummary(value.getCourseSummary())
				.courseHighlight(value.getCourseHighlight()).faqs(value.getFaqs()).build();

		// upload icon and set icon url in course object
		String folder = "COURSE/";
		folder += course.getCourseName();
		String iconUrl = awss3Service.uploadFile(icon, folder);

		if (!iconUrl.isEmpty()) {
			course.setCourseIcon(iconUrl);
		} else {
			throw new NullPointerException("Icon can't be uploaded due to admin restriction");
		}

		// upload course images and set respective urls in course object
		String folder2 = "COURSE/";
		folder2 += "IMAGE/" + course.getCourseName();

		String imageUrl = awss3Service.uploadFile(image, folder2);

		String homePageImageUrl = awss3Service.uploadFile(homePageImage, folder2);

		if (!imageUrl.isEmpty() && !homePageImageUrl.isEmpty()) {
			course.setCourseImage(imageUrl);
			course.setHomePageCourseImage(homePageImageUrl);
		} else {
			throw new NullPointerException("Icon can't be uploaded due to admin restriction");
		}

		course = setCourseIntoFaq(course);
		course = saveCourse(course);
		return course;

	}

	@Override
	public ResponseEntity<ApiResponse<Course>> updateCourseAlongWithImages(UpdateCourseDto updateCourseDto) {

		String courseRequest = updateCourseDto.getCourseContent();
		MultipartFile icon = updateCourseDto.getIcon();
		MultipartFile image = updateCourseDto.getImage();
		MultipartFile homePageImage = updateCourseDto.getHomePageImage();

		// string of json body to object
		ObjectMapper objectMapper = new ObjectMapper();
		UpdateCourseRequestDto value;
		try {
			value = objectMapper.readValue(courseRequest, UpdateCourseRequestDto.class);
		} catch (JsonProcessingException e) {
			System.err.println(e.getMessage());
			throw new InvalidInfoException("The Json body format is incorrect");
		}

		Course course = courseDao.fetchCourseById(value.getCourseId())
				.orElseThrow(() -> new IdNotFoundException("Course With the Given Id Not Found"));

		if (icon != null) {
			// upload icon and set icon url in course object
			String folder = "COURSE/";
			folder += course.getCourseName();
			String iconUrl = awss3Service.uploadFile(icon, folder);

			if (!iconUrl.isEmpty()) {
				course.setCourseIcon(iconUrl);
			} else {
				throw new NullPointerException("Icon can't be uploaded due to admin restriction");
			}
		}

		if (image != null) {
			// upload course images and set respective urls in course object
			String folder2 = "COURSE/";
			folder2 += "IMAGE/" + course.getCourseName();

			String imageUrl = awss3Service.uploadFile(image, folder2);

			if (!imageUrl.isEmpty()) {
				course.setCourseImage(imageUrl);
			} else {
				throw new NullPointerException("Icon can't be uploaded due to admin restriction");
			}
		}

		if (homePageImage != null) {
			// upload course images and set respective urls in course object
			String folder2 = "COURSE/";
			folder2 += "IMAGE/" + course.getCourseName();

			String homePageImageUrl = awss3Service.uploadFile(homePageImage, folder2);

			if (!homePageImageUrl.isEmpty()) {
				course.setHomePageCourseImage(homePageImageUrl);
			} else {
				throw new NullPointerException("Icon can't be uploaded due to admin restriction");
			}
		}

		// updated course field is set to the course

		course.setCourseName(value.getCourseName());
		course.setCourseDescription(value.getCourseDescription());
		course.setBranchType(value.getBranchType());
		course.setMode(value.getMode());
		course.setCourseSummary(value.getCourseSummary());
		course.setCourseAbout(value.getCourseAbout());
		course.setCourseHighlight(value.getCourseHighlight());
		value.getFaqs().forEach(f -> f.setCourse(course));
		course.setFaqs(value.getFaqs());
		Course saveCourse = courseDao.saveCourse(course);
		return ResponseUtil.getOk(saveCourse);
	}

	@Override
	public ResponseEntity<ApiResponse<Course>> updateCourseContent(CourseIdResponse dto) {

		Course course = courseDao.fetchCourseById(dto.getCourseId())
				.orElseThrow(() -> new IdNotFoundException("Course With the Given Id Not Found"));
		if (dto.getBranchType().size() != 1) {
			throw new InvalidInfoException("Organization type entry not valid");
		}
		course.setCourseName(dto.getCourseName());
		course.setMode(dto.getMode());
		course.setCourseDescription(dto.getCourseDescription());
		course.setCourseAbout(dto.getCourseAbout());
		course.setCourseSummary(dto.getCourseSummary());
		course.setCourseHighlight(dto.getCourseHighlight());
		dto.getFaqs().forEach(f -> f.setCourse(course));
		course.setFaqs(dto.getFaqs());
		course.setBranchType(dto.getBranchType());

		return ResponseUtil.getOk(courseDao.saveCourse(course));
	}

	@Override
	public ResponseEntity<ApiResponse<String>> removeSubjectsFromCourse(Long courseId, List<Long> subjectIds) {
		if (courseDao.isCoursePresent(courseId)) {
			subjectIds.stream().forEach(subjectId -> {
				if (!subjectDao.isSubjectPresent(subjectId))
					throw new IdNotFoundException("Subject With the Given Id  " + subjectId + " Not Found");

				if (!courseDao.isSubjectIdPresent(courseId, subjectId))
					throw new DuplicateDataInsertionException(
							"Given subjectId " + subjectId + " not mapped to any course");
			});

			courseDao.removeSubjectsFromCourse(courseId, subjectIds);
			return ResponseUtil.getNoContent("Removed the given subject Ids from the particular course");
		}

		throw new IdNotFoundException("Course With the Given Id Not Found");
	}

	private Course mapAndSetUrlsToCourse(CourseRequestImageDto courseRequest) {
		MultipartFile icon = courseRequest.getIcon();
		MultipartFile image = courseRequest.getImage();
		MultipartFile homePageImage = courseRequest.getHomePageImage();

		// Log the received files and JSON body
		log.info("Received icon file: {}", icon.getOriginalFilename());
		log.info("Received image file: {}", image.getOriginalFilename());
		log.info("Received homePageImage file: {}", homePageImage.getOriginalFilename());
		log.debug("Received course JSON: {}", courseRequest.getCourse());

		// Convert JSON body to object
		ObjectMapper objectMapper = new ObjectMapper();
		CourseRequestDto value;
		try {
			value = objectMapper.readValue(courseRequest.getCourse(), CourseRequestDto.class);
			log.debug("Parsed CourseRequestDto: {}", value);
		} catch (JsonProcessingException e) {
			log.error("Error parsing JSON: {}", e.getMessage());
			throw new InvalidInfoException("The Json body format is incorrect");
		}

		// DTO to Course
		Course course = Course.builder().courseName(value.getCourseName())
				.courseDescription(value.getCourseDescription()).branchType(value.getBranchType()).mode(value.getMode())
				.courseAbout(value.getCourseAbout()).courseSummary(value.getCourseSummary())
				.courseHighlight(value.getCourseHighlight()).faqs(value.getFaqs()).build();
		log.debug("Mapped course object: {}", course);

		// Upload icon and set icon URL in course object
		String folder = "COURSE/" + course.getCourseName();
		String iconUrl = awss3Service.uploadFile(icon, folder);
		log.info("Uploaded icon URL: {}", iconUrl);

		if (!iconUrl.isEmpty()) {
			course.setCourseIcon(iconUrl);
		} else {
			log.error("Icon upload failed due to admin restriction");
			throw new NullPointerException("Icon can't be uploaded due to admin restriction");
		}

		// Upload course images and set respective URLs in course object
		String folder2 = "COURSE/IMAGE/" + course.getCourseName();
		String imageUrl = awss3Service.uploadFile(image, folder2);
		String homePageImageUrl = awss3Service.uploadFile(homePageImage, folder2);
		log.info("Uploaded course image URL: {}", imageUrl);
		log.info("Uploaded homePageImage URL: {}", homePageImageUrl);

		if (!imageUrl.isEmpty() && !homePageImageUrl.isEmpty()) {
			course.setCourseImage(imageUrl);
			course.setHomePageCourseImage(homePageImageUrl);
		} else {
			log.error("Image upload failed due to admin restriction");
			throw new NullPointerException("Icon can't be uploaded due to admin restriction");
		}

		// Set course into FAQ and save course
		course = setCourseIntoFaq(course);
		log.debug("Set course into FAQ: {}", course);
		course = saveCourse(course);
		log.info("Saved course: {}", course);

		return course;
	}

	@Override
	public ResponseEntity<ApiResponse<Course>> saveCourseAlongWithImages(CourseRequestImageDto courseRequestDto) {
		long categoryId = courseRequestDto.getCategoryId();
		Long subCategoryId = courseRequestDto.getSubCategoryId();

		log.info("Received request to save course along with images");
		log.debug("CourseRequestImageDto: {}", courseRequestDto);
		log.info("Category ID: {}", categoryId);
		log.info("SubCategory ID: {}", subCategoryId);

		List<Long> subjectIds = courseRequestDto.getSubjectIds();
		List<Subject> subjects = new ArrayList<Subject>();
		if (Objects.nonNull(subjectIds)) {
			subjects = subjectDao.fetchSubjectsByIds(subjectIds);
		}

		if (categoryDao.isCategoryPresent(categoryId)) {
			log.info("Category with ID {} is present", categoryId);

			if (subCategoryId != null) {
				if (subCategoryDao.isSubCategoryPresent(subCategoryId)) {
					log.info("SubCategory with ID {} is present", subCategoryId);

					Course course = mapAndSetUrlsToCourse(courseRequestDto);

					subCategoryDao.assignCourseToSubCategory(subCategoryId, course.getCourseId());
					log.info("Assigned course with ID {} to subCategory with ID {}", course.getCourseId(),
							subCategoryId);

					List<Subject> collect = subjects.stream().peek(subject -> subject.getCourses().add(course))
							.collect(Collectors.toList());
					course.setSubjects(collect);

					return ResponseUtil.getCreated(course);
				} else {
					log.warn("No SubCategory found with given ID: {}", subCategoryId);
					throw new IdNotFoundException("No SubCategory found with given Id: " + subCategoryId);
				}
			} else {
				Course course = mapAndSetUrlsToCourse(courseRequestDto);

				categoryDao.assignCourseToCategory(categoryId, course.getCourseId());
				log.info("Assigned course with ID {} to category with ID {}", course.getCourseId(), categoryId);

				return ResponseUtil.getCreated(course);
			}
		} else {
			log.error("No Category found with given ID: {}", categoryId);
			throw new IdNotFoundException("No Category found with given Id: " + categoryId);
		}
	}

}
