package com.alpha.qspiderrestapi.service;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Subject;

public interface SubjectService {

	ResponseEntity<ApiResponse<Subject>> saveSubject(Subject subject);

}
