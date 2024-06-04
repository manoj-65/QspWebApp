package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.SubTopic;

public interface SubTopicDao {

	SubTopic saveSubTopic(SubTopic subTopic);

	Optional<SubTopic> fetchSubTopicById(long subTopicId);

	List<SubTopic> fetchAllSubTopics();

	void deleteSubTopic(long subTopicId);

	long isSubTopicPresent(long subTopicId);
}
