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
public class TopicDto {

	private String topicTitle;
	private String topicDescription;
	private long topicModuleCount;
	private double topicModuleDuration;
	private List<SubTopicDto> subTopics = new ArrayList<SubTopicDto>();
	private String topicPreviewUrl;
	private double topicPreviewDuration;

}
