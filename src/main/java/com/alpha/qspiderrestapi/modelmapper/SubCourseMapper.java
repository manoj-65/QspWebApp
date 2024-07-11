package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.SubCourseResponse;
import com.alpha.qspiderrestapi.entity.Course;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Component
public class SubCourseMapper {
	
	@Autowired
	private WeightageMapper weightageMapper;

	public SubCourseResponse mapToSubCourseResponse(Course course,long subCategoryId) {
		return SubCourseResponse.builder().subCourseResponseId(course.getCourseId()).icon(course.getCourseIcon())
				.title(course.getCourseName()).description(course.getCourseDescription())
				.homePageCourseImage(course.getHomePageCourseImage())
				.modes(course.getMode())
				.weightageDto(weightageMapper.getDto(course.getWeightages(), 0l, Long.valueOf(subCategoryId)))
				.build();
	}

	public List<SubCourseResponse> mapToSubCourseResponseList(List<Course> courses, long subCategoryId) {
		List<SubCourseResponse> subCourseList = new ArrayList<SubCourseResponse>();
		courses.stream().forEach((course) -> subCourseList.add(mapToSubCourseResponse(course,subCategoryId)));
		return subCourseList;
	}
}
