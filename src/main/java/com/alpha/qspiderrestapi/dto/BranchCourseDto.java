package com.alpha.qspiderrestapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchCourseDto {

	private String cityName;
	private int numberOfBranches;
	private String cityImage;
	
}
