package com.alpha.qspiderrestapi.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.Chapter;
import com.alpha.qspiderrestapi.entity.Subject;

import jakarta.transaction.Transactional;

/**
 * Utility class for mapping Subject to its Chapters.
 * 
 * @see TopicUtil
 */
@Component
public class ChapterUtil {

	@Autowired
	private TopicUtil topicUtil;
	
	 /**
     * Maps a Subject to its Chapters, setting the Subject reference in each Chapter.
     * Also maps each Chapter to its Topics.
     *
     * @param subject the Subject entity to map.
     * @return the mapped Subject entity with its Chapters.
     */
	public Subject mapSubjectToChapters(Subject subject) {

		List<Chapter> chapterList = Optional.ofNullable(subject.getChapters())
		.filter(chapters -> !chapters.isEmpty())
		.map(chapters -> chapters.stream()
							   .peek(chapter -> {
								   chapter.setSubject(subject);
								   topicUtil.mapChapterToTopics(chapter);
								   })
							   .collect(Collectors.toList()))
							   .orElse(Collections.emptyList());

		subject.setChapters(chapterList);
		return subject;
	}
}
