package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

	/**
	 * Finds a subject by its ID.
	 *
	 * This method fetches a subject record from the database based on the provided
	 * subject ID.
	 *
	 * @param subjectId The ID of the subject to retrieve.
	 * @return An Integer representing the subject ID if found, otherwise null.
	 * @throws NonexistentEntityException (or a more specific exception type) If no
	 *                                    subject is found with the provided ID.
	 */
	@Query(value = "Select s.subjectId From Subject s Where s.subjectId = :subjectId")
	Long findBySubjectId(@Param("subjectId") long subjectId);
}
