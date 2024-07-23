package com.alpha.qspiderrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.WeightageService;

@RestController
@RequestMapping("api/{version}/weightage")
public class WeightageController {

	@Autowired
	private WeightageService weightageService;

	@PostMapping("/categories")
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(@PathVariable String version,
			@RequestParam long categoryId, @RequestBody WeightageDto dto) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCategoryWeightage(categoryId, dto);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PostMapping("/subCategories")
	public ResponseEntity<ApiResponse<Weightage>> saveSubCategoryWeightage(@PathVariable String version,
			@RequestParam long categoryId, @RequestParam long subCategoryId, @RequestBody WeightageDto dto) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveSubCategoryWeightage(categoryId, subCategoryId, dto);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PostMapping("/courses")
	public ResponseEntity<ApiResponse<Weightage>> saveCourseWeightage(@PathVariable String version,
			@RequestParam long categoryId, @RequestParam(required = false) Long subCategoryId,
			@RequestParam long courseId, @RequestBody WeightageDto dto) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCourseWeightage(categoryId, subCategoryId, courseId, dto);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PostMapping("/city")
	public ResponseEntity<ApiResponse<Weightage>> saveCityWeightage(@PathVariable String version,
			@RequestParam String cityName, @RequestBody WeightageDto dto) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCityWeightage(cityName, dto);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@DeleteMapping("/removeCategoryWeightage")
	public ResponseEntity<ApiResponse<String>> deleteCategoryWeightage(@PathVariable String version,
			@RequestParam Long categoryId) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.deleteCategoryWeightage(categoryId);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PatchMapping("/subCategories")
	public ResponseEntity<ApiResponse<String>> updateSubCategoryWeightage(@PathVariable String version,
			@RequestParam long categoryId, @RequestParam long subCategoryId, @RequestParam Organization organization,
			@RequestParam long weightage) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.updateSubCategoryWeightage(categoryId, subCategoryId, organization, weightage);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@DeleteMapping("/removeSubCategoryWeightage")
	public ResponseEntity<ApiResponse<String>> deleteSubCategoryWeightage(@PathVariable String version,
			@RequestParam Long subCategoryId) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.deleteSubCategoryWeightage(subCategoryId);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PatchMapping("/updateCategoryWeightage")
	public ResponseEntity<ApiResponse<String>> updateCategoryWeightage(@PathVariable String version,
			@RequestParam Long categoryId, @RequestParam Long weightage, Organization organisation) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.updateCategoryWeightage(categoryId, weightage, organisation);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PatchMapping("/courses")
	public ResponseEntity<ApiResponse<String>> updateCourseWeightage(@PathVariable String version,
			@RequestParam long categoryId, @RequestParam(required = false) Long subCategoryId,
			@RequestParam long courseId, @RequestBody WeightageDto weightageDto) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.updateCourseWeightage(categoryId, subCategoryId, courseId, weightageDto);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PostMapping("/categoryWeightage")
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightageAndIncrement(@PathVariable String version,
			@RequestParam long categoryId, @RequestBody WeightageDto dto) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCategoryWeightageAndIncrement(categoryId, dto);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PostMapping("/country")
	public ResponseEntity<ApiResponse<Weightage>> saveCountryWeightage(@PathVariable String version,
			@RequestParam String countryName, @RequestBody WeightageDto dto) {
		if (version.equalsIgnoreCase("V1"))
			return weightageService.saveCountryWeightage(countryName, dto);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

}
