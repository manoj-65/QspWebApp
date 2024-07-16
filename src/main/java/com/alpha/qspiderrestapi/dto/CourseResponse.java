package com.alpha.qspiderrestapi.dto;

import java.util.List;

import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.enums.Mode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

	private long courseResponseId;
	private String icon;
	private String title;
	private String image_url;
	private String description;
	private String homePageCourseImage;
	private List<Mode> modes;
	private WeightageDto weightageDto;

}
