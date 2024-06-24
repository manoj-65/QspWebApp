package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alpha.qspiderrestapi.dto.BranchById_CourseDto;
import com.alpha.qspiderrestapi.entity.Batch;

public class BranchById_CourseDtoMapper {

	public static List<BranchById_CourseDto> mapToBranchById_CourseDto(List<Batch> batches) {
		Map<Long, List<Batch>> uniqueBatchMap = batches.stream().collect(Collectors.groupingBy(b->b.getCourse().getCourseId()));
		List<BranchById_CourseDto> courses = new ArrayList<BranchById_CourseDto>();
		uniqueBatchMap.forEach((courseId,uniqueBatches)->{
			courses.add(BranchById_CourseDto.builder()
								.courseId(courseId)
								.courseName(uniqueBatches.get(0).getCourse().getCourseName())
								.courseImage(uniqueBatches.get(0).getCourse().getCourseImage())
								.homePageCourseImage(uniqueBatches.get(0).getCourse().getHomePageCourseImage())
								.courseDescription(uniqueBatches.get(0).getCourse().getCourseDescription())
								.build());
		});
		
		return courses;
	}

}
