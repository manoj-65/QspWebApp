package com.alpha.qspiderrestapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Weightage;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("api/{version}/weightage")
public class WeightageController {

	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(@PathVariable String version,
																		   @RequestParam long categoryId,
																		   @RequestBody WeightageDto dto){
		return null;
	}
}
