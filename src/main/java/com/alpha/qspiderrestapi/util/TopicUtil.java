package com.alpha.qspiderrestapi.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.Chapter;
import com.alpha.qspiderrestapi.entity.Topic;

import jakarta.transaction.Transactional;

/**
 * Utility class for mapping Chapter to its Topics.
 * 
 * @see SubTopicUtil
 */
@Component
public class TopicUtil {

	@Autowired
	private SubTopicUtil subTopicUtil;

	/**
     * Maps a Chapter to its Topics, setting the Chapter reference in each Topic.
     * Also maps each Topic to its SubTopics.
     *
     * @param chapter the Chapter entity to map.
     * @return the mapped Chapter entity with its Topics.
     */
	public Chapter mapChapterToTopics(Chapter chapter) {

		List<Topic> topicList = Optional.ofNullable(chapter.getTopics()).filter(topics -> !topics.isEmpty())
				.map(topics -> topics.stream().peek(topic -> {
					topic.setChapter(chapter);
					subTopicUtil.mapTopicToSubTopics(topic);
				}).collect(Collectors.toList())).orElse(Collections.emptyList());
		chapter.setTopics(topicList);
		return chapter;
	}
}
