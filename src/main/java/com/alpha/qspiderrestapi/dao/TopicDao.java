package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Topic;

public interface TopicDao {

	Topic saveTopic(Topic topic);

	Optional<Topic> fetchTopicById(long topicId);

	List<Topic> fetchAllTopics();

	void deleteTopic(long topicId);

	long isTopicPresent(long topicId);
}
