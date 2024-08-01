package com.alpha.qspiderrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.FaqDto;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.FaqService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/faqs")
public class FaqController {

	@Autowired
	private FaqService faqService;

	@PostMapping
	public ResponseEntity<ApiResponse<List<Faq>>> saveFaq(@PathVariable String version, @RequestBody List<FaqDto> faqs,
			@RequestParam Organization organizationType) {
		if (version.equalsIgnoreCase("V1"))
			return faqService.saveFaq(faqs, organizationType);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<Faq>>> fetchAllFaqByOrganisation(@PathVariable String version,
			@RequestHeader String origin) {
		if (version.equalsIgnoreCase("V1"))
			return faqService.fetchAllFaqs(origin);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}

}