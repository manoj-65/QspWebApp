package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CourseDto {

	private long courseId;
	private String courseName;
	private String courseDescription;
	private String courseIcon;
	private long cQspiders;
	private long cJspiders;
	private long cPyspiders;
	private long cBspiders;
	private List<BranchDto> branches;

}
