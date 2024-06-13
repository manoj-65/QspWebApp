package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.CourseIdResponse;
import com.alpha.qspiderrestapi.entity.Course;

public interface CourseService {

	ResponseEntity<ApiResponse<Course>> saveCourse(long categoryId, Long subCategoryId, Course course);

	ResponseEntity<ApiResponse<List<Course>>> fetchAllCourse();

	ResponseEntity<ApiResponse<CourseIdResponse>> fetchCourseById(long courseId);

	ResponseEntity<ApiResponse<Course>> assignSubjectsToCourse(long courseId, List<Long> subjectIds);

	ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long categoryId);

	ResponseEntity<ApiResponse<String>> removeCourseById(long courseId);

}
