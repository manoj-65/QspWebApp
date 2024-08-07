package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Course;

public interface CourseDao {

	Course saveCourse(Course course);

	Optional<Course> fetchCourseById(long courseId);

	List<Course> fetchAllCourses();

	void deleteCourse(long courseId);

	boolean isCoursePresent(long courseId);

	void assignSubjectToCourse(long courseId, long subjectId);

	boolean isSubjectIdPresent(long courseId, long subjectId);

	void deleteCourse(Course fetchedCourse);

	boolean isCourseExist(long courseId);

	void removeCourseAndSubCategoryById(long courseId);

	void removeCourseAndCategoryById(long courseId);

	void removeSubjectsFromCourse(Long courseId, List<Long> subjectIds);

}
