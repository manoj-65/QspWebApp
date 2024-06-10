package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Subject;

public interface SubjectService {

	ResponseEntity<ApiResponse<Subject>> saveSubject(Subject subject);

	ResponseEntity<ApiResponse<Subject>> fetchSubjectById(long subjectId);

	ResponseEntity<ApiResponse<List<Subject>>> fetchAllSubjectsOfCourse(long courseId);

	ResponseEntity<ApiResponse<List<Subject>>> fetchAllSubjects();

}
