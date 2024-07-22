package com.alpha.qspiderrestapi.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.alpha.qspiderrestapi.entity.enums.Organization;

import lombok.Data;

@Data
public class CourseRequestDto {

	private String courseName;
	private String courseDescription;
	private List<Organization> branchType;
	private List<Mode> mode;
	private String courseSummary;
	private String courseAbout;
	private String courseHighlight;
	private List<Faq> faqs;
	private MultipartFile icon;
	private MultipartFile image;
	private MultipartFile homePageImage;
	private long categoryId;
	private Long subCategoryId;
	
}
