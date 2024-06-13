package com.alpha.qspiderrestapi.dto;

import java.util.ArrayList;
import java.util.List;

import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.entity.enums.Mode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseIdResponse {

	private long courseId;
	private String courseName;
	private List<Mode> mode;
	private String courseSummary;
	private String courseAbout;
	private String courseHighlight;
	private List<Faq> faqs = new ArrayList<Faq>();
	private String courseImage;
	private List<BranchCourseDto> branches;
	private List<BatchDto> onlineBatches;
	private List<Subject> subjects;
}
