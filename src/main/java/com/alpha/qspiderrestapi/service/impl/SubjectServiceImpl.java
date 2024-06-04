package com.alpha.qspiderrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.SubjectDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.service.SubjectService;
import com.alpha.qspiderrestapi.util.ChapterUtil;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the SubjectService interface.
 * Provides methods to handle business logic related to Subjects.
 * 
 * @see ChapterUtil
 */
@Service
@Slf4j
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectDao subjectDao;

	@Autowired
	private ChapterUtil chapterUtil;

	 /**
     * Saves a new Subject and also the chapters, topics, and subTopics if present.
     * Before saving maps Subject to all its chapter.
     * @param subject the Subject entity to be saved.
     * @return a ResponseEntity containing the ApiResponse with the saved Subject.
     */
	@Override
	public ResponseEntity<ApiResponse<Subject>> saveSubject(Subject subject) {
		try {
			return ResponseUtil.getCreated(subjectDao.saveSubject(chapterUtil.mapSubjectToChapters(subject)));
		}
		catch(Exception e) {
			log.error("Error occurred while saving Subject",e.getMessage());
			throw e;
		}
	}

}
