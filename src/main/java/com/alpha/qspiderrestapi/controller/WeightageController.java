package com.alpha.qspiderrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.WeightageService;

@RestController
@RequestMapping("api/{version}/weightage")
public class WeightageController {
	
	@Autowired
	private WeightageService weightageService;

	@PostMapping("/categories")
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(@PathVariable String version,
																		   @RequestParam long categoryId,
																   @RequestBody WeightageDto dto){
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCategoryWeightage(categoryId,dto);

		throw new UnauthorizedVersionException("Unauthorized Version");		
	}
}
