package com.alpha.qspiderrestapi.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UpdateCourseDto {
	
	private String courseContent; 
	private MultipartFile icon;
	private MultipartFile image;
	private MultipartFile homePageImage;
}
