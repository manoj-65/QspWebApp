package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.TopicDao;
import com.alpha.qspiderrestapi.entity.Topic;
import com.alpha.qspiderrestapi.repository.TopicRepository;

@Repository
public class TopicDaoImpl implements TopicDao {

	@Autowired
	TopicRepository topicRepository;

	@Override
	public Topic saveTopic(Topic topic) {
		return topicRepository.save(topic);
	}

	@Override
	public Optional<Topic> fetchTopicById(long topicId) {
		return topicRepository.findById(topicId);
	}

	@Override
	public List<Topic> fetchAllTopics() {
		return topicRepository.findAll();
	}

	@Override
	public void deleteTopic(long topicId) {
		topicRepository.deleteById(topicId);
	}

	@Override
	public long isTopicPresent(long topicId) {
		return topicRepository.findByTopicId(topicId);
	}

}
