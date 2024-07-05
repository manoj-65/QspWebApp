
package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.alpha.qspiderrestapi.dto.BranchDto;
import com.alpha.qspiderrestapi.dto.CourseIdResponse;
import com.alpha.qspiderrestapi.dto.ViewAllHomePageResponse;
import com.alpha.qspiderrestapi.entity.CityBranchView;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.entity.ViewAllHomePage;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.DuplicateDataInsertionException;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.modelmapper.CourseMapper;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.CourseService;
import com.alpha.qspiderrestapi.util.ChapterUtil;
import com.alpha.qspiderrestapi.util.CourseUtil;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.ViewHomePageUtil;

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
		List<Faq> faqs = course.getFaqs().stream().peek(faq -> faq.setCourse(course)).toList();
		course.setFaqs(faqs);
		return course;
	}

	private Course saveCourse(Course course) {
		if (!course.getSubjects().isEmpty()) {
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
	public ResponseEntity<ApiResponse<List<Course>>> fetchAllCourse() {
		return ResponseUtil.getOk(courseDao.fetchAllCourses());
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
			CourseIdResponse courseResponse = CourseMapper.mapToCourseDto(course);
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

	@Override
	public ResponseEntity<ApiResponse<List<ViewAllHomePageResponse>>> fetchViewForHomepage(String hostName) {
		Organization orgType = util.checkOrganizationType(hostName);
		List<ViewAllHomePage> var = viewDao.fetchAllViewByCityName(orgType);
		List<BranchDto> branchDtos = courseUtil.getTheBranchDto(var);

		Map<String, List<BranchDto>> cityBranchListMap = branchDtos.stream()
				.collect(Collectors.groupingBy(BranchDto::getCity));

		List<ViewAllHomePageResponse> response = new ArrayList<>();

		String cityName = null;
		for (Entry<String, List<BranchDto>> branchTypeEntry : cityBranchListMap.entrySet()) {
			ViewAllHomePageResponse pageResponse = new ViewAllHomePageResponse();
			pageResponse.setCityName(branchTypeEntry.getKey());
			pageResponse.setBranches(branchTypeEntry.getValue());
			response.add(pageResponse);

		} 

		// Convert BranchDto objects to Branch objects (assuming conversion logic)
//		for (BranchDto dto : branchDtos) {
//			branches.add(dto);
//		}

		// Create and populate ViewAllHomePageResponse object

		// Now set the list of Branch objects

//			Map<String, BranchDto> test = new HashMap<String, BranchDto>();
//			for (BranchDto branchDto: branches) {
//				test.put(branchDto.getCity(), branchDto);
//			}

//			List<ViewAllHomePageResponse> response = new ArrayList<ViewAllHomePageResponse>();
		//
//			for (Entry<String, Map<String, List<BranchDto>>> entry : cityBranchMap.entrySet()) {
//				ViewAllHomePageResponse pageResponse = new ViewAllHomePageResponse();
//				pageResponse.setBranches(entry.getValue());
//				pageResponse.setCityName(entry.getKey());
//				response.add(pageResponse);
		//
//			}

		return ResponseUtil.getOk(response);
	}
}
