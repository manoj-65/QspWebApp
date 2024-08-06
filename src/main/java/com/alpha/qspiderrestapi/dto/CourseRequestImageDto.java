package com.alpha.qspiderrestapi.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CourseRequestImageDto {

	private String course;
	private MultipartFile icon;
	private MultipartFile image;
	private MultipartFile homePageImage;
	private long categoryId;
	private Long subCategoryId;
	private List<Long> subjectIds;
}
