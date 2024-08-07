package com.alpha.qspiderrestapi.dto;

import java.util.List;

import com.alpha.qspiderrestapi.entity.enums.Mode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCourseResponse {

	private long subCourseResponseId;
	private String icon;
	private String title;
	private String description;
	private String homePageCourseImage;
	private List<Mode> modes;
	private WeightageDto weightageDto;
	private int subjectCount;

}
