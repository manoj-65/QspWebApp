package com.alpha.qspiderrestapi.dto;

import java.util.ArrayList;
import java.util.List;

import com.alpha.qspiderrestapi.entity.CityBranchView;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.alpha.qspiderrestapi.entity.enums.Organization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseIdResponse {

	private long courseId;
	private String courseName;
	private List<Mode> mode;
	private String courseDescription;
	private String courseSummary;
	private String courseAbout;
	private String courseHighlight;
	private List<Faq> faqs = new ArrayList<Faq>();
	private List<Organization> branchType;
	private String courseImage;
	private List<CityBranchView> branches;
	private List<BatchDto> onlineBatches;
	private List<SubjectDto> subjects;
}
