package com.alpha.qspiderrestapi.dto;

import java.util.List;

import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.alpha.qspiderrestapi.entity.enums.Organization;

import lombok.Data;

@Data
public class CourseRequestDto {

	private String courseName;
	private String courseDescription;
	private Organization branchType;
	private List<Mode> mode;
	private String courseSummary;
	private String courseAbout;
	private String courseHighlight;
	private List<Faq> faqs;

}
