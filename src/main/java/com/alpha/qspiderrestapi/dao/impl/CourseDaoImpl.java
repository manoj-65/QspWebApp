package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.repository.CourseRepository;

@Repository
public class CourseDaoImpl implements CourseDao {

	@Autowired
	CourseRepository courseRepository;

	@Override
	public Course saveCourse(Course course) {
		return courseRepository.save(course);
	}

	@Override
	public Optional<Course> fetchCourseById(long courseId) {
		return courseRepository.findById(courseId);
	}

	@Override
	public List<Course> fetchAllCourses() {
		return courseRepository.findAll();
	}

	@Override
	public void deleteCourse(long courseId) {
		courseRepository.deleteById(courseId);
	}

	@Override
	public boolean isCoursePresent(long courseId) {
		return courseRepository.findByCourseId(courseId) != null;
	}

	@Override
	public void assignSubjectToCourse(long courseId, long subjectId) {
		courseRepository.assignSubjectToCourse(courseId, subjectId);
	}

	@Override
	public boolean isSubjectIdPresent(long courseId, long subjectId) {
		if (courseRepository.findBySubjectId(subjectId, courseId) == null)
			return false;
		return true;
	}

	@Override
	public void deleteCourse(Course course) {
		courseRepository.delete(course);
	}
	
	@Override
	public boolean isCourseExist(long courseId) {
		return courseRepository.existsById(courseId);
	}

	@Override
	public void removeCourseAndSubCategoryById(long courseId) {
		 courseRepository.removeCourseAndSubCategoryById(courseId);
	}
	
	@Override
	public void removeCourseAndCategoryById(long courseId) {
		 courseRepository.removeCourseAndCategoryById(courseId);
	}

}
