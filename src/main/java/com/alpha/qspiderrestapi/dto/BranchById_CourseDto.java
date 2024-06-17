package com.alpha.qspiderrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchById_CourseDto {

	private long courseId;
	private String courseImage;
	private String courseName;
	private String courseDescription;
}
