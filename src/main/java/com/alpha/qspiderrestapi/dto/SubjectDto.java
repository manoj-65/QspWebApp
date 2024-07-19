package com.alpha.qspiderrestapi.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
	private String subjectTitle;
	private String subjectDescription;
	private long subjectModuleCount;
	private double subjecModuleDuration;
	private List<ChapterDto> chapters = new ArrayList<ChapterDto>();
}