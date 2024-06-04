package com.alpha.qspiderrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCourseResponse {

	private long subCourseResponseId;
	private String icon;
	private String title;
	private String description;
}
