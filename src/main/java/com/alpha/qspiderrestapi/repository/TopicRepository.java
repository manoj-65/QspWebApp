package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {

	@Query(value = "Select t.topicId From Topic t Where t.topicId = :topicId")
	Long findByTopicId(@Param("topicId") long topicId);
}
