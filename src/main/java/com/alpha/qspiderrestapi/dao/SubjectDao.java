package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Subject;

public interface SubjectDao {

	Subject saveSubject(Subject subject);

	Optional<Subject> fetchSubjectById(long subjectId);

	List<Subject> fetchAllSubjects();

	void deleteSubject(long subjectId);

	boolean isSubjectPresent(long subjectId);

	int removeSubjectAndCourseById(long subjectId);
}
