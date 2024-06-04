package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.SubjectDao;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.repository.SubjectRepository;

import jakarta.transaction.Transactional;

/**
 * Implementation of SubjectDao interface
 * 
 * Provides methods to interact with SubjectRepository interface
 * 
 * @see SubjectRepository
 */
@Repository
public class SubjectDaoImpl implements SubjectDao {

	@Autowired
	SubjectRepository subjectRepository;

	/**
	 * Saves a Subject entity.
	 *
	 * @param subject the Subject entity to be saved.
	 * @return the saved Subject entity.
	 */
	@Override
	public Subject saveSubject(Subject subject) {
		return subjectRepository.save(subject);
	}

	/**
	 * Fetches a Subject entity by its ID.
	 *
	 * @param subjectId the ID of the Subject to fetch.
	 * @return an Optional containing the fetched Subject, or empty if not found.
	 */
	@Override
	public Optional<Subject> fetchSubjectById(long subjectId) {
		return subjectRepository.findById(subjectId);
	}

	/**
	 * Fetches all Subject entities.
	 *
	 * @return a List of all Subject entities.
	 */
	@Override
	public List<Subject> fetchAllSubjects() {
		return subjectRepository.findAll();
	}

	/**
	 * Deletes a Subject entity by its ID.
	 *
	 * @param subjectId the ID of the Subject to delete.
	 */
	@Override
	public void deleteSubject(long subjectId) {
		subjectRepository.deleteById(subjectId);
	}

	/**
	 * Checks if a Subject entity is present by its ID.
	 *
	 * @param subjectId the ID of the Subject to check.
	 * @return true if the Subject entity is present, false otherwise.
	 */
	@Override
	public boolean isSubjectPresent(long subjectId) {
		return subjectRepository.findBySubjectId(subjectId) != null;
	}

}
