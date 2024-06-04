package com.alpha.qspiderrestapi.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.SubTopic;
import com.alpha.qspiderrestapi.entity.Topic;

import jakarta.transaction.Transactional;

/**
 * Utility class for mapping Topic to its SubTopics.
 */
@Component
public class SubTopicUtil {

	/**
     * Maps a Topic to its SubTopics, setting the Topic reference in each SubTopic.
     *
     * @param topic the Topic entity to map.
     * @return the mapped Topic entity with its SubTopics.
     */
	public Topic mapTopicToSubTopics(Topic topic) {
		if (!(topic.getSubTopics().isEmpty())) {
			List<SubTopic> subTopicList = topic.getSubTopics().stream().peek(subTopic -> subTopic.setTopic(topic))
					.collect(Collectors.toList());
			topic.setSubTopics(subTopicList);
			return topic;
		}
		return topic;
	}
}
