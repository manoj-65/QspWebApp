package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.SubTopicDto;
import com.alpha.qspiderrestapi.entity.SubTopic;

@Component
public class SubTopicMapper {

	public List<SubTopicDto> mapSubTopicToSubTopicDto(List<SubTopic> subTopics) {

		List<SubTopicDto> subTopicDtoList = new ArrayList<SubTopicDto>();
		subTopics.stream().forEach(subTopic -> {
			subTopicDtoList.add(SubTopicDto.builder().subTopicTitle(subTopic.getSubTopicTitle())
					.subTopicDescription(subTopic.getSubTopicDescription())
					.subTopicPreviewUrl(subTopic.getSubTopicPreviewUrl())
					.subTopicPreviewDuration(subTopic.getSubTopicPreviewDuration()).build());

		});
		return subTopicDtoList;

	}

}
