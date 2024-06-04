package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.SubTopicDao;
import com.alpha.qspiderrestapi.entity.SubTopic;
import com.alpha.qspiderrestapi.repository.SubTopicRepository;

@Repository
public class SubTopicDaoImpl implements SubTopicDao {

	@Autowired
	SubTopicRepository subTopicRepository;

	@Override
	public SubTopic saveSubTopic(SubTopic subTopic) {
		return subTopicRepository.save(subTopic);
	}

	@Override
	public Optional<SubTopic> fetchSubTopicById(long subTopicId) {
		return subTopicRepository.findById(subTopicId);
	}

	@Override
	public List<SubTopic> fetchAllSubTopics() {
		return subTopicRepository.findAll();
	}

	@Override
	public void deleteSubTopic(long subTopicId) {
		subTopicRepository.deleteById(subTopicId);
	}

	@Override
	public long isSubTopicPresent(long subTopicId) {
		return subTopicRepository.findBySubTopicId(subTopicId);
	}
}
