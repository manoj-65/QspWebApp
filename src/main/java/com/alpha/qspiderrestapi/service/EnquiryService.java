package com.alpha.qspiderrestapi.service;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Enquiry;

public interface EnquiryService {

	ResponseEntity<ApiResponse<String>> saveEnquiry(Enquiry enquiry);
}
