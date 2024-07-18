package com.alpha.qspiderrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.SubCategoryService;
import com.alpha.qspiderrestapi.service.impl.SubCategoryServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for handling SubCategory-related endpoints.
 * 
 * @see ApiResponse
 * @see SubCategoryServiceImpl
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/subcategories")
public class SubCategoryController {

	@Autowired
	private SubCategoryService subCategoryService;

	/**
	 * Endpoint to save a SubCategory entity.
	 *
	 * @param version     the API version.
	 * @param categoryId  the ID of the Category to which the SubCategory belongs.
	 * @param subCategory the SubCategory entity to be saved.
	 * @return a ResponseEntity containing the ApiResponse with the saved
	 *         SubCategory.
	 * @throws UnauthorizedVersionException if the API version is not authorized.
	 */

	@Operation(description = "A SubCategory associated with a category is saved into the database", summary = "Saves a SubCategory")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping
	public ResponseEntity<ApiResponse<SubCategory>> saveSubCategory(@PathVariable String version,
			@RequestParam long categoryId, @RequestBody SubCategory subCategory) {
		if (version.equalsIgnoreCase("V1"))
			return subCategoryService.saveSubCategory(categoryId, subCategory);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	/**
	 * Endpoint to fetch a SubCategory entity by its ID.
	 *
	 * @param version       the API version.
	 * @param subCategoryId the ID of the SubCategory to fetch.
	 * @return a ResponseEntity containing the ApiResponse with the fetched
	 *         SubCategory.
	 * @throws UnauthorizedVersionException if the API version is not authorized.
	 */
	@Operation(description = "A SubCategory is fetched using id from the database", summary = "fetch a SubCategory")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "OK", responseCode = "200"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@GetMapping
	public ResponseEntity<ApiResponse<SubCategory>> fetchSubCategoryById(@PathVariable String version,
			@RequestParam long subCategoryId) {
		if (version.equalsIgnoreCase("V1"))
			return subCategoryService.fetchSubCategoryById(subCategoryId);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@Operation(description = "Assigns courses to a Sub-Category", summary = "Updates associated courses")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "OK", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400") })
	@PatchMapping(value = "/assigncourses")
	public ResponseEntity<ApiResponse<SubCategory>> assignCoursesToSubCategory(@PathVariable String version,
			@RequestParam int subCategoryId, @RequestBody List<Long> courseIds) {
		if (version.equalsIgnoreCase("V1"))
			return subCategoryService.assignCoursesToSubCategory(subCategoryId, courseIds);

		throw new UnauthorizedVersionException("Unauthorized Version");

	}

	@Operation(description = "Sub-Category icon url is added to a Sub-Category", summary = "Updates the Sub-Category icon")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400") })
	@PatchMapping(value = "/uploadIcon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadIcon(@PathVariable String version,
			@RequestParam("file") MultipartFile file, @RequestParam long subCategoryId) {
		if (version.equals("v1"))
			return subCategoryService.uploadIcon(file, subCategoryId);
		throw new UnauthorizedVersionException();
	}
 
	@DeleteMapping("/removeCourseFromSubCategory")
	public ResponseEntity<ApiResponse<String>> removeCourseFromSubCategory(@PathVariable String version,
			@RequestParam Long subCategoryId, @RequestBody List<Long> courseIds) {
		if (version.equals("v1"))
			return subCategoryService.removeCourseFromCategory(subCategoryId, courseIds);

		throw new UnauthorizedVersionException();
	}
}
