package com.alpha.qspiderrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubTopicDto {

	private String subTopicTitle;
	private String subTopicDescription;
	private long subTopicModuleCount;
	private double subTopicModuleDuration;
	private String subTopicPreviewUrl;
	private double subTopicPreviewDuration;
}
