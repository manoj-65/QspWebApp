package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CourseDto {

	private long courseId;
	private String courseName;
	private List<BranchDto> branches;
}
