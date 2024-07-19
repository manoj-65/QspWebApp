package com.alpha.qspiderrestapi.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {

	private String chapterTitle;
	private String chapterDescription;
	private long chapterModuleCount;
	private double chapterModuleDuration;
	private List<TopicDto> topics = new ArrayList<TopicDto>();
	private String chapterPreviewUrl;
	private double chapterPreviewDuration;
}
