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
public class SubCategoryResponse {

	private long subCourseId;
	private String icon;
	private String title;

	private List<SubCourseResponse> subCourseResponse;
}
