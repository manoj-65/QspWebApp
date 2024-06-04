package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.SubTopic;

public interface SubTopicRepository extends JpaRepository<SubTopic, Long> {

	@Query(value = "Select s.subTopicId From SubTopic s Where s.subTopicId = :subTopicId")
	Long findBySubTopicId(@Param("subTopicId") long subTopicId);
}
