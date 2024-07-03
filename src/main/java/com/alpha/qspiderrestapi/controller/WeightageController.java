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
	
	@PostMapping("/subCategories")
	public ResponseEntity<ApiResponse<Weightage>> saveSubCategoryWeightage(@PathVariable String version,
																		   @RequestParam long categoryId,
																		   @RequestParam long subCategoryId,
																   @RequestBody WeightageDto dto){
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveSubCategoryWeightage(categoryId,subCategoryId,dto);

		throw new UnauthorizedVersionException("Unauthorized Version");		
	}
	
	@PostMapping("/courses")
	public ResponseEntity<ApiResponse<Weightage>> saveCourseWeightage(@PathVariable String version,
																		   @RequestParam long categoryId,
																		   @RequestParam(required = false) Long subCategoryId,
																		   @RequestParam long courseId,
																   @RequestBody WeightageDto dto){
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCourseWeightage(categoryId,subCategoryId,courseId,dto);

		throw new UnauthorizedVersionException("Unauthorized Version");		
	}
	
	@PostMapping("/city")
	public ResponseEntity<ApiResponse<Weightage>> saveCityWeightage(@PathVariable String version,
																		   @RequestParam String cityName,
																   @RequestBody WeightageDto dto){
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCityWeightage(cityName,dto);

		throw new UnauthorizedVersionException("Unauthorized Version");		
	}
}
