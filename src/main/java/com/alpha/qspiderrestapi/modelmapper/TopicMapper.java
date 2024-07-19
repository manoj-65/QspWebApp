package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.TopicDto;
import com.alpha.qspiderrestapi.entity.SubTopic;
import com.alpha.qspiderrestapi.entity.Topic;
import com.alpha.qspiderrestapi.util.SubjectUtil;

@Component
public class TopicMapper {

	@Autowired
	private SubTopicMapper subTopicMapper;

	@Autowired
	private SubjectUtil subjectUtil;

	public List<TopicDto> mapTopicToTopicDto(List<Topic> topics) {

		List<TopicDto> topicDtoList = new ArrayList<TopicDto>();
		topics.stream().forEach(topic -> {
			topicDtoList.add(TopicDto.builder().topicTitle(topic.getTopicTitle())
					.topicDescription(topic.getTopicDescription()).topicPreviewUrl(topic.getTopicPreviewUrl())
					.topicPreviewDuration(topic.getTopicPreviewDuration())
					.subTopics(subTopicMapper.mapSubTopicToSubTopicDto(topic.getSubTopics()))
					.topicModuleCount(subjectUtil.getModuleCount(topic.getSubTopics()))
					.topicModuleDuration(
							subjectUtil.getModuleDuration(topic.getSubTopics(), SubTopic::getSubTopicPreviewDuration))
					.build());
		});
		return topicDtoList;
	}

}
