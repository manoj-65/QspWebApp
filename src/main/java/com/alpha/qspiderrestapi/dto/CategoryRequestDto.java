package com.alpha.qspiderrestapi.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CategoryRequestDto {

	private String title;
	private MultipartFile icon;
	private MultipartFile alternativeIcon;
}
