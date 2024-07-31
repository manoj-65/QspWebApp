package com.alpha.qspiderrestapi.service.impl;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.BatchDao;
import com.alpha.qspiderrestapi.dao.BranchDao;
import com.alpha.qspiderrestapi.dao.CityDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.BatchRequestDto;
import com.alpha.qspiderrestapi.entity.Batch;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.enums.BatchStatus;
import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
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

	@Autowired
	private CityDao cityDao;

	@Override
	public ResponseEntity<ApiResponse<Batch>> saveBatch(Long branchId, long courseId, BatchRequestDto batch) {
		log.info("Saving batch for branch ID: {} and course ID: {}", branchId, courseId);
		
		if(batch.getStartingDate().isAfter(LocalDate.now().minusDays(1)) && batch.getEndingDate().isBefore(batch.getStartingDate()))
			throw new InvalidInfoException("Invalid start and end dates");
		
		if(batch.getEndingTime().isBefore(batch.getStartingTime()))
			throw new InvalidInfoException("Invalid start and end times");
		
		Course course = courseDao.fetchCourseById(courseId).orElseThrow(() -> {
			log.error("Course not found with ID: {}", courseId);
			return new IdNotFoundException("No Course Found with id: " + courseId);
		});
		Branch branch = null;
		Mode batchMode = Mode.ONLINE_CLASSES;
		if (branchId != null) {
			
			branch = branchDao.fetchBranchById(branchId).orElseThrow(() -> {
				log.error("Branch not found with ID: {}", branchId.longValue());
				return new IdNotFoundException("No Branch Found with id: " + branchId);
			});
			System.err.println(branch.getBranchType().equals(course.getBranchType().get(0))+" "+branch.getBranchType()+" "+course.getBranchType().get(0));
			if (branch.getBranchType().equals(course.getBranchType().get(0))) {
				batchMode = Mode.OFFLINE_CLASSES;
			} else {
				throw new InvalidInfoException("Given course and branch belong to different Organisation Type");
			}
		}
		if (course.getMode().contains(batchMode)) {
			Batch builtBatch = Batch.builder().batchTitle(batch.getBatchTitle()).trainerName(batch.getTrainerName())
					.startingDate(batch.getStartingDate()).endingDate(batch.getEndingDate())
					.startingTime(batch.getEndingTime()).endingTime(batch.getStartingTime()).batchMode(batchMode)
					.batchStatus(BatchStatus.UPCOMING).extendingDays(batch.getExtendingDays()).branch(branch)
					.course(course).build();

			builtBatch = batchDao.saveBatch(builtBatch);
			log.info("Batch saved successfully: {}", batch);
			return ResponseUtil.getCreated(builtBatch);
		}
		throw new InvalidInfoException("Given course and batch mode is not matching");
	}

	@Override
	@Scheduled(cron = "0 0 2 ? * MON,THU")
	public void updateBatchStatus() {

		batchDao.updateBatchStatus(BatchStatus.ONGOING, BatchStatus.BLOCKED);
		batchDao.updateBatchStatus(BatchStatus.UPCOMING, BatchStatus.ONGOING);
		cityDao.updateCityBranchCount();
	}
	
	@Override
	@Scheduled(cron = "0 0 2 * * ?")
	public void updateBatchStatuses() {

		batchDao.updateToOngoing();
		batchDao.updateToBlocked();
		cityDao.updateCityBranchCount();
	}

	@Override
	@Scheduled(cron = "0 0 3 ? * MON,THU")
	public void createBatch() {
		List<String> branchTypes = Arrays.asList("JSP", "QSP", "PYSP", "PROSP"); // Example branch types
		Time startTime1 = Time.valueOf("10:00:00"); // Start time 10:00 AM
		Time startTime2 = Time.valueOf("14:00:00"); // Start time 2:00 PM

		batchDao.createBatches(branchTypes, startTime1, startTime2);
		cityDao.updateCityBranchCount();
	}

	@Override
	public ResponseEntity<ApiResponse<String>> deleteBatch(long batchId) {
		if (batchDao.isBatchPresent(batchId)) {
			batchDao.deleteBatch(batchId);
		}

		return ResponseUtil.getNoContent("Deletion Successful");
	}

}
