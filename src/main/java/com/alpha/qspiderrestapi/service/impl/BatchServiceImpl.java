package com.alpha.qspiderrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.BatchDao;
import com.alpha.qspiderrestapi.dao.BranchDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Batch;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.service.BatchService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BatchServiceImpl implements BatchService {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private BranchDao branchDao;

	@Autowired
	private BatchDao batchDao;

	@Override
	public ResponseEntity<ApiResponse<Batch>> saveBatch(long branchId, long courseId, Batch batch) {
		log.info("Saving batch for branch ID: {} and course ID: {}", branchId, courseId);
		Course course = courseDao.fetchCourseById(courseId).orElseThrow(() -> {
			log.error("Course not found with ID: {}", courseId);
			return new IdNotFoundException("No Course Found with id: " + courseId);
		});

		Branch branch = branchDao.fetchBranchById(branchId).orElseThrow(() -> {
			log.error("Branch not found with ID: {}", branchId);
			return new IdNotFoundException("No Branch Found with id: " + branchId);
		});

		batch.setCourse(course);
		batch.setBranch(branch);
		batch = batchDao.saveBatch(batch);
		log.info("Batch saved successfully: {}", batch);

		return ResponseUtil.getCreated(batch);
	}

}
