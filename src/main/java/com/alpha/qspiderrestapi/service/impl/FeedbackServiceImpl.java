package com.alpha.qspiderrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.FeedbackDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.FeedBack;
import com.alpha.qspiderrestapi.exception.InvalidPhoneNumberException;
import com.alpha.qspiderrestapi.service.FeedbackSevice;
import com.alpha.qspiderrestapi.service.MailService;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.ValidatePhoneNumber;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackSevice {

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private MailService mailSender;
	@Autowired
	private ValidatePhoneNumber validatePhoneNumber;

	@Override
	public ResponseEntity<ApiResponse<FeedBack>> saveFeedback(FeedBack feedBack) {
		if (validatePhoneNumber.isValidPhoneNumber(feedBack.getPhoneNumber())) {
			log.info("The process of saving feedback has been initiated......");
			feedBack = feedbackDao.saveFeedback(feedBack);
			try {
				FeedbackEmailData data = new FeedbackEmailData(feedBack);
				mailSender.sendMail(data);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			log.info("The process of saving feedback has been completed.");
			return ResponseUtil.getCreated(feedBack);
		}
		log.error("FeedBack has invalid Phone Number");
		throw new InvalidPhoneNumberException("Kindly enter a valid Phone Number.");

	}

}
