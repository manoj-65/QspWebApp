package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CourseFormResponseDto {

	private long course_id;
	private String course_icon;
	private String course_name;
	private int subjectCount;
	private List<CategoryFormResponseDto> categories;
	private List<SubCategoryFormResponseDto> subCategories;
	
}
