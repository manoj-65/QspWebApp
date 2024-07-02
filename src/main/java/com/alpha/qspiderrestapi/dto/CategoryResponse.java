package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

	private long courseId;
	private String icon;
	private String alternativeIcon;
	private String title;

	private List<SubCategoryResponse> subCourse;
	private List<CourseResponse> courseResponse;

}
