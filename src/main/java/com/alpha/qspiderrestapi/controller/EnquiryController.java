package com.alpha.qspiderrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Enquiry;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.EnquiryService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/enquiry")
public class EnquiryController {

	@Autowired
	private EnquiryService enquiryService;

	@PostMapping
	public ResponseEntity<ApiResponse<String>> saveEnquiry(@PathVariable String version,
			@Valid @RequestBody Enquiry enquiry) {
		if (version.equalsIgnoreCase("v1"))
			return enquiryService.saveEnquiry(enquiry);
		throw new UnauthorizedVersionException("Unauthorized Version");

	}
}
