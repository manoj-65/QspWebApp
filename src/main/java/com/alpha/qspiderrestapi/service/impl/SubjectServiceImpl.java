package com.alpha.qspiderrestapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.SubjectDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.service.SubjectService;
import com.alpha.qspiderrestapi.util.ChapterUtil;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the SubjectService interface. Provides methods to handle
 * business logic related to Subjects.
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

	@Autowired
	private CourseDao courseDao;

	/**
	 * Saves a new Subject and also the chapters, topics, and subTopics if present.
	 * Before saving maps Subject to all its chapter.
	 * 
	 * @param subject the Subject entity to be saved.
	 * @return a ResponseEntity containing the ApiResponse with the saved Subject.
	 */
	@Override
	public ResponseEntity<ApiResponse<Subject>> saveSubject(Subject subject) {

		Subject subjectEntity = subjectDao.saveSubject(chapterUtil.mapSubjectToChapters(subject));
		try {
			return ResponseUtil.getCreated(subjectEntity);
		} catch (Exception e) {
			log.error("Error occurred while saving Subject", e.getMessage());
			throw e;
		}
	}

	@Override
	public ResponseEntity<ApiResponse<Subject>> fetchSubjectById(long subjectId) {
		log.info("Entering  fetchSubjectById with Subject id: {}", subjectId);
		try {
			log.info("Subject of the with the id: {} is returned", subjectId);
			return ResponseUtil.getOk(subjectDao.fetchSubjectById(subjectId)
					.orElseThrow(() -> new IdNotFoundException("No Id Found with the given Id: " + subjectId)));
		} catch (IdNotFoundException e) {
			log.error("Error occured : {}", e.getMessage());
			throw e;
		}

	}

	@Override
	public ResponseEntity<ApiResponse<List<Subject>>> fetchAllSubjectsOfCourse(long courseId) {
		log.info("Entering  fetchAllSubjectsOfCourse with course id: {}", courseId);
		try {
			List<Subject> subjects = courseDao.fetchCourseById(courseId)
					.orElseThrow(() -> new IdNotFoundException("No Id Found with the given Id: " + courseId))
					.getSubjects();
			log.info("All the subjects of the given course id is returned");
			return ResponseUtil.getOk(subjects);
		} catch (IdNotFoundException e) {
			log.error("Error occured : {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public ResponseEntity<ApiResponse<List<Subject>>> fetchAllSubjects() {
		return ResponseUtil.getOk(subjectDao.fetchAllSubjects());
	}

	@Override
	public ResponseEntity<ApiResponse<String>> deleteById(long subjectId) {
		Optional<Subject> subject = subjectDao.fetchSubjectById(subjectId);

		if (subject.isPresent()) {
			if (!(subject.get().getCourses().isEmpty()))
				subjectDao.removeSubjectAndCourseById(subjectId); // removes the subject link with every course present
			subjectDao.deleteSubject(subjectId);
		} else
			throw new IdNotFoundException("Given Subject Id: " + subjectId + " not found");
		return ResponseUtil.getNoContent("Subject deleted");
	}

}
