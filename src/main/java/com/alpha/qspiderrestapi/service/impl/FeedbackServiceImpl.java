package com.alpha.qspiderrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.FeedbackDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.FeedBack;
import com.alpha.qspiderrestapi.service.FeedbackSevice;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackSevice {

	@Autowired
	private FeedbackDao feedbackDao;

	@Override
	public ResponseEntity<ApiResponse<FeedBack>> saveFeedback(FeedBack feedBack) {
		log.info("The process of saving feedback has been initiated......");
		feedBack = feedbackDao.saveFeedback(feedBack);
		log.info("The process of saving feedback has been completed.");
		return ResponseUtil.getCreated(feedBack);
	}

}
