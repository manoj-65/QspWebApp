package com.alpha.qspiderrestapi.service;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.FeedBack;

public interface FeedbackSevice {

	ResponseEntity<ApiResponse<FeedBack>> saveFeedback(FeedBack feedBack);

}
