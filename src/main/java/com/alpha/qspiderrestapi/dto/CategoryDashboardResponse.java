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

public class CategoryDashboardResponse {
	
	private long categoryId;
	private String categoryName;
	private String categoryAlternativeIcon;
	private List<CourseResponse> courses;
}
