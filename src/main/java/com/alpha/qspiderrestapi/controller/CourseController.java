package com.alpha.qspiderrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CourseIdResponse;
import com.alpha.qspiderrestapi.dto.CourseRequestImageDto;
import com.alpha.qspiderrestapi.dto.UpdateCourseDto;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;

	/**
	 * Saves a new course associated with a specific category.
	 *
	 * This method handles HTTP POST requests to create a new course and associate
	 * it with an existing category. The course data should be provided in the
	 * request body.
	 *
	 * @param course     The course data to be saved. This data should be provided
	 *                   in the request body as JSON or another supported format.
	 * @param categoryId The ID of the category to associate the course with. This
	 *                   ID should be provided as a request parameter.
	 * @param version    API version specified in the path variable. Currently only
	 *                   version "v1" is supported.
	 * @return A ResponseEntity object containing the saved course information and
	 *         the HTTP status code. The status code will be: * 201 Created - if the
	 *         course is saved successfully. * 400 Bad Request - if the request body
	 *         is invalid, missing required fields, or the category ID is invalid. *
	 *         401 Unauthorized - if an unsupported API version is specified.
	 * @throws Exception If an unexpected error occurs during the saving process.
	 */
	@Operation(description = "A Course associated with a category is saved into the database", summary = "Saves a Course")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping
	public ResponseEntity<ApiResponse<Course>> saveCourse(@PathVariable String version, @RequestParam long categoryId,
			@RequestParam(required = false) Long subCategoryId, @RequestBody Course course) {

		if (version.equals("v1"))
			return courseService.saveCourse(categoryId, subCategoryId, course);

		throw new UnauthorizedVersionException();
	}

	/**
	 * Retrieves all available courses.
	 *
	 * This method handles HTTP GET requests to retrieve a list of all courses.
	 *
	 * @param version API version specified in the path variable. Currently only
	 *                version "v1" is supported.
	 * @return A ResponseEntity object containing a list of `Course` objects and the
	 *         HTTP status code. The status code will be: * 200 OK - if courses are
	 *         retrieved successfully. * 401 Unauthorized - if an unsupported API
	 *         version is specified.
	 * @throws Exception If an unexpected error occurs during course retrieval.
	 */
	@Operation(description = "Fetches all course from the database", summary = "Fetches all Course")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Ok", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@GetMapping("/getall")
	public ResponseEntity<ApiResponse<List<Course>>> fetchAllCourses(@PathVariable String version) {
		if (version.equals("v1"))
			return courseService.fetchAllCourse();

		throw new UnauthorizedVersionException();
	}

	/**
	 * Retrieves a course by its ID.
	 *
	 * This method handles HTTP GET requests to retrieve a specific course based on
	 * its unique identifier.
	 *
	 * @param courseId The ID of the course to retrieve. This ID should be provided
	 *                 as a request parameter.
	 * @param version  API version specified in the path variable. Currently only
	 *                 version "v1" is supported.
	 * @return A ResponseEntity object containing the retrieved course information
	 *         and the HTTP status code. The status code will be: * 200 OK - if the
	 *         course is found successfully. * 400 Bad Request - if the course ID is
	 *         invalid. * 401 Unauthorized - if an unsupported API version is
	 *         specified. * 404 Not Found - if no course is found with the provided
	 *         ID.
	 * @throws Exception If an unexpected error occurs during course retrieval.
	 */
	@Operation(description = "Fetches Course based on the courseid from the database", summary = "Fetches a Course")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Ok", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@GetMapping("/getbyid")
	public ResponseEntity<ApiResponse<CourseIdResponse>> fetchCourseById(@RequestParam long courseId,
			@PathVariable String version) {
		if (version.equals("v1"))
			return courseService.fetchCourseById(courseId);

		throw new UnauthorizedVersionException();
	}

	@Operation(description = "Assigins subjects to course based on Id", summary = "Updates or Saves a subject to course")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "OK", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PatchMapping
	public ResponseEntity<ApiResponse<Course>> assignSubjectsToCourse(@PathVariable String version,
			@RequestParam long courseId, @RequestBody List<Long> subjectIds) {
		if (version.equalsIgnoreCase("V1"))
			return courseService.assignSubjectsToCourse(courseId, subjectIds);

		throw new UnauthorizedVersionException("Unauthorized Version");

	}

	@Operation(description = "Course Icon url is added to a Course", summary = "Updates Course Icon")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400") })
	@PatchMapping(value = "/uploadIcon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadIcon(@PathVariable String version,
			@RequestParam("file") MultipartFile file, @RequestParam long courseId) {
		if (version.equals("v1"))
			return courseService.uploadIcon(file, courseId);
		throw new UnauthorizedVersionException();
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<String>> removeCourseById(@PathVariable String version,
			@RequestParam long courseId) {
		if (version.equals("v1"))
			return courseService.removeCourseById(courseId);

		throw new UnauthorizedVersionException();
	}

	@PatchMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadImage(@PathVariable String version,
			@RequestParam("imagefile") MultipartFile imagefile,
			@RequestParam("homePageImagefile") MultipartFile homePageImage, @RequestParam long courseId) {
		if (version.equals("v1"))
			return courseService.uploadImages(imagefile, homePageImage, courseId);
		throw new UnauthorizedVersionException();
	}

//	@GetMapping("/viewAll")
//	public ResponseEntity<ApiResponse<List<ViewAllHomePageResponse>>> viewAllResponseHomePage(
//			@PathVariable String version, @RequestHeader("Origin") String hostName) {
//		if (version.equals("v1"))
//			return courseService.fetchViewForHomepage(hostName);
//
//		throw new UnauthorizedVersionException();
//	}

//	@PostMapping(value = "/saveCourse")
//	public ResponseEntity<ApiResponse<Course>> saveCourseAlongWithImages(@PathVariable String version,
//			@RequestParam long categoryId, @RequestParam(required = false) Long subCategoryId,
//			@RequestPart MultipartFile icon, @RequestPart MultipartFile image,
//			@RequestPart MultipartFile homePageImage, @RequestPart String course) {
////		long categoryId=3l;
////		Long subCategoryId=null;
//		if (version.equals("v1"))
//			return courseService.saveCourseAlongWithImages(categoryId, subCategoryId, course, icon, image,
//					homePageImage);
//
//		throw new UnauthorizedVersionException();
//	}

	@PostMapping(value = "/saveCourse")
	public ResponseEntity<ApiResponse<Course>> saveCourseAlongWithImages(@PathVariable String version,
			@ModelAttribute CourseRequestImageDto dto) {

		log.info("Received request to save course along with images, version: {}", version);
		log.debug("CourseRequestImageDto: {}", dto);

		if (version.equals("v1")) {
			log.info("Processing saveCourseAlongWithImages for version v1");
			ResponseEntity<ApiResponse<Course>> response = courseService.saveCourseAlongWithImages(dto);
			log.info("Response from courseService: {}", response);
			return response;
		}

		log.warn("Unauthorized version: {}", version);
		throw new UnauthorizedVersionException();
	}

	@PutMapping(value = "/updateCourse")
	public ResponseEntity<ApiResponse<Course>> updateCourseAlongWithImages(@PathVariable String version,
			@ModelAttribute UpdateCourseDto course) {
		if (version.equals("v1"))
			return courseService.updateCourseAlongWithImages(course);

		throw new UnauthorizedVersionException();
	}

	@PutMapping(value = "/updateCourseContent")
	public ResponseEntity<ApiResponse<Course>> updateCourseContent(@PathVariable String version,
			@RequestBody CourseIdResponse courseIdResponse) {
		if (version.equals("v1"))
			return courseService.updateCourseContent(courseIdResponse);

		throw new UnauthorizedVersionException();
	}

	@DeleteMapping("/removeSubjectsFromCourse")
	public ResponseEntity<ApiResponse<String>> removeSubjectsFromCourse(@PathVariable String version,
			@RequestParam Long courseId, @RequestBody List<Long> subjectIds) {
		if (version.equals("v1"))
			return courseService.removeSubjectsFromCourse(courseId, subjectIds);

		throw new UnauthorizedVersionException();
	}

}
