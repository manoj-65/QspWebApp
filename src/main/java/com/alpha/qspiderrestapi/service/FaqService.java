package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.FaqDto;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.Organization;

public interface FaqService {

	ResponseEntity<ApiResponse<List<Faq>>> saveFaq(List<FaqDto> faqs, Organization organizationType);

	ResponseEntity<ApiResponse<List<Faq>>> fetchAllFaqs(Organization organization);
}
