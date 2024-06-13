package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import com.alpha.qspiderrestapi.dto.CourseIdResponse;
import com.alpha.qspiderrestapi.dto.CourseResponse;
import com.alpha.qspiderrestapi.entity.Course;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseMapper {

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
	public static CourseResponse mapToCourseResponse(Course course) {
		return CourseResponse.builder().courseResponseId(course.getCourseId()).icon(course.getCourseIcon())
				.title(course.getCourseName()).description(course.getCourseDescription()).build();
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
	public static List<CourseResponse> mapToCourseResponseList(List<Course> courses) {

		List<CourseResponse> responseList = new ArrayList<CourseResponse>();

		courses.forEach(response -> responseList.add(mapToCourseResponse(response)));
		return responseList;

	}

	public static CourseIdResponse mapToCourseDto(Course course) {

		return CourseIdResponse.builder().courseId(course.getCourseId()).courseName(course.getCourseName())
				.mode(course.getMode()).courseSummary(course.getCourseSummary()).courseAbout(course.getCourseAbout())
				.courseHighlight(course.getCourseHighlight()).faqs(course.getFaqs())
				.courseImage(course.getCourseImage()).branchType(course.getBranchType()).build();

	}

}
