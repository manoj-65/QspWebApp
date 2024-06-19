package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import com.alpha.qspiderrestapi.dto.SubCourseResponse;
import com.alpha.qspiderrestapi.entity.Course;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubCourseMapper {

	public static SubCourseResponse mapToSubCourseResponse(Course course) {
		return SubCourseResponse.builder().subCourseResponseId(course.getCourseId()).icon(course.getCourseIcon())
				.title(course.getCourseName()).description(course.getCourseDescription())
				.homePageCourseImage(course.getHomePageCourseImage())
				.modes(course.getMode()).build();
	}

	public static List<SubCourseResponse> mapToSubCourseResponseList(List<Course> courses) {
		List<SubCourseResponse> subCourseList = new ArrayList<SubCourseResponse>();
		courses.stream().forEach((course) -> subCourseList.add(mapToSubCourseResponse(course)));
		return subCourseList;
	}
}
