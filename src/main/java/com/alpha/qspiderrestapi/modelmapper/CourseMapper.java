package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.CourseIdResponse;
import com.alpha.qspiderrestapi.dto.CourseResponse;
import com.alpha.qspiderrestapi.entity.Course;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Component
public class CourseMapper {

	@Autowired
	private WeightageMapper weightageMapper;

	@Autowired
	private SubjectMapper subjectMapper;

	/**
	 * Converts a Course entity to a CourseResponse DTO.
	 *
	 * This method transforms a `Course` object into a `CourseResponse` Data
	 * Transfer Object (DTO). The DTO contains relevant information about the
	 * course, excluding potentially sensitive data.
	 * 
	 * @param course The Course entity to be converted.
	 * @return A CourseResponse DTO containing the mapped data.
	 */
	public CourseResponse mapToCourseResponse(Course course, long categoryId) {
		return CourseResponse.builder().courseResponseId(course.getCourseId()).icon(course.getCourseIcon())
				.image_url(course.getCourseImage()).title(course.getCourseName())
				.description(course.getCourseDescription()).homePageCourseImage(course.getHomePageCourseImage())
				.modes(course.getMode()).weightageDto(weightageMapper.getDto(course.getWeightages(), categoryId, null))
				.build();
	}

	/**
	 * Converts a list of Course entities to a list of CourseResponse DTOs.
	 *
	 * This method iterates over a list of `Course` entities and transforms each one
	 * into a `CourseResponse` DTO using the `mapToCourseResponse` method. The
	 * resulting list contains DTOs representing all the courses in the input list.
	 * 
	 * @param courses The list of Course entities to be converted.
	 * @return A list of CourseResponse DTOs containing the mapped data.
	 */
	public List<CourseResponse> mapToCourseResponseList(List<Course> courses, long categoryId) {

		List<CourseResponse> responseList = new ArrayList<CourseResponse>();

		courses.forEach(response -> responseList.add(mapToCourseResponse(response, categoryId)));
		return responseList;

	}

	public CourseIdResponse mapToCourseDto(Course course) {

		return CourseIdResponse.builder().courseId(course.getCourseId()).courseName(course.getCourseName())
				.mode(course.getMode()).courseDescription(course.getCourseDescription())
				.courseSummary(course.getCourseSummary()).courseAbout(course.getCourseAbout())
				.courseHighlight(course.getCourseHighlight()).faqs(course.getFaqs())
				.courseImage(course.getCourseImage()).branchType(course.getBranchType())
				.courseImage(course.getCourseImage())
				.subjects(subjectMapper.mapSubjectToSubjectDto(course.getSubjects())).build();

	}

}
