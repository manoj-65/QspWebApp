package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CourseFormResponseDto;
import com.alpha.qspiderrestapi.dto.CourseIdResponse;
import com.alpha.qspiderrestapi.dto.CourseRequestDto;
import com.alpha.qspiderrestapi.dto.CourseRequestImageDto;
import com.alpha.qspiderrestapi.dto.UpdateCourseDto;
import com.alpha.qspiderrestapi.dto.ViewAllHomePageResponse;
import com.alpha.qspiderrestapi.entity.Course;

public interface CourseService {

	ResponseEntity<ApiResponse<Course>> saveCourse(long categoryId, Long subCategoryId, Course course);

	ResponseEntity<ApiResponse<List<CourseFormResponseDto>>> fetchAllCourse();

	ResponseEntity<ApiResponse<CourseIdResponse>> fetchCourseById(long courseId);

	ResponseEntity<ApiResponse<Course>> assignSubjectsToCourse(long courseId, List<Long> subjectIds);

	ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long categoryId);

	ResponseEntity<ApiResponse<String>> removeCourseById(long courseId);

	ResponseEntity<ApiResponse<String>> uploadImages(MultipartFile image, MultipartFile homePageImage, long courseId);

	ResponseEntity<ApiResponse<List<ViewAllHomePageResponse>>> fetchViewForHomepage(String hostName);

	ResponseEntity<ApiResponse<Course>> saveCourseAlongWithImages(long categoryId, Long subCategoryId, String course,
			MultipartFile icon, MultipartFile image, MultipartFile homePageImage);

	ResponseEntity<ApiResponse<Course>> updateCourseContent(CourseIdResponse courseIdResponse);

	ResponseEntity<ApiResponse<String>> removeSubjectsFromCourse(Long courseId, List<Long> subjectIds);

	ResponseEntity<ApiResponse<Course>> saveCourseAlongWithImages(CourseRequestImageDto courseRequestDto);

	ResponseEntity<ApiResponse<Course>> updateCourseAlongWithImages(UpdateCourseDto updateCourseDto);

}
