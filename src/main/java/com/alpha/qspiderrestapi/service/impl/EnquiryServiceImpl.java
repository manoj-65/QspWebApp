package com.alpha.qspiderrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.EnquiryDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Enquiry;
import com.alpha.qspiderrestapi.entity.enums.EnquiryType;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.exception.InvalidPhoneNumberException;
import com.alpha.qspiderrestapi.service.EnquiryService;
import com.alpha.qspiderrestapi.service.MailService;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.ValidatePhoneNumber;

import jakarta.mail.MessagingException;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private EnquiryDao enquiryDao;

	@Autowired
	private MailService mailSender;

	@Autowired
	private ValidatePhoneNumber validatePhoneNumber;

	@Override
	public ResponseEntity<ApiResponse<String>> saveEnquiry(Enquiry enquiry) {
		if (validatePhoneNumber.isValidPhoneNumber(enquiry.getMobileNumber())) {
			if (enquiry.getEnquiryType().equals(EnquiryType.HIREFROMUS)
					&& (enquiry.getCompanyName() == null || enquiry.getCompanyName().isBlank())) {
				throw new InvalidInfoException("Enter the Company Name");
			} else if (enquiry.getEnquiryType().equals(EnquiryType.CORPORATETRAINING)
					&& (enquiry.getRequiredTraining() == null || enquiry.getRequiredTraining().isBlank())) {
				throw new InvalidInfoException("Enter Training is Required Or Not");
			}
			enquiryDao.save(enquiry);
			try {
				EnquiryEmailData data = new EnquiryEmailData(enquiry);
				mailSender.sendMail(data);
			} catch (MessagingException e) {
				e.printStackTrace();
			}

			return ResponseUtil.getCreated("Saved Successfully");

		}
		throw new InvalidPhoneNumberException("Enter a valid Phone Number");

	}

}
