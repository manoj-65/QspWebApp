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
import com.alpha.qspiderrestapi.entity.FeedBack;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.FeedbackSevice;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/feedback")
public class FeedbackController {

	@Autowired
	private FeedbackSevice feedbackSevice;

	@PostMapping
	public ResponseEntity<ApiResponse<FeedBack>> saveFeedBack(@PathVariable String version,
			@Valid @RequestBody FeedBack feedBack) {
		if (version.equalsIgnoreCase("v1"))
			return feedbackSevice.saveFeedback(feedBack);
		throw new UnauthorizedVersionException("Unauthorized Version");

	}

}
