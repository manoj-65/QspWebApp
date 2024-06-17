package com.alpha.qspiderrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.alpha.qspiderrestapi.dto.CategoryFormResponse;
import com.alpha.qspiderrestapi.dto.CategoryResponse;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * Saves a new category.
	 *
	 * This method handles HTTP POST requests to create a new category. It expects
	 * the category data to be provided in the request body.
	 *
	 * @param version  API version specified in the path variable. Currently only
	 *                 version "V1" is supported.
	 * @param category The category data to be saved. This data should be provided
	 *                 in the request body as JSON or another supported format.
	 * @return A ResponseEntity object containing the saved category information and
	 *         the HTTP status code. The status code will be: * 201 Created - if the
	 *         category is saved successfully. * 400 Bad Request - if the request
	 *         body is invalid or missing required fields. * 401 Unauthorized - if
	 *         an unsupported API version is specified.
	 * @throws Exception If an unexpected error occurs during the saving process.
	 */
	@Operation(description = "A Category is saved into the database", summary = "Saves a Category")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping
	public ResponseEntity<ApiResponse<Category>> saveCategory(@PathVariable String version,
			@RequestBody Category category) {
		if (version.equalsIgnoreCase("V1"))
			return categoryService.saveCategory(category);

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	/**
	 * Retrieves all categories.
	 *
	 * This method handles HTTP GET requests to retrieve all available categories.
	 *
	 * @param version API version specified in the path variable. Currently only
	 *                version "V1" is supported.
	 * @return A ResponseEntity object containing a list of `CategoryResponse`
	 *         objects and the HTTP status code. The status code will be: * 200 OK -
	 *         if categories are retrieved successfully. * 401 Unauthorized - if an
	 *         unsupported API version is specified.
	 * @throws Exception If an unexpected error occurs during category retrieval.
	 */
	@Operation(description = "Fetches all the category from the database", summary = "Fetches all Categories")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Ok", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@GetMapping("/getall")
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> fetchAllCategories(@PathVariable String version) {
		if (version.equalsIgnoreCase("V1"))
			return categoryService.fetchAllCategories();

		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	/**
	 * Retrieves all categories.
	 *
	 * This method handles HTTP GET requests to retrieve category based on Id.
	 *
	 * @param version API version specified in the path variable. Currently only
	 *                version "V1" is supported.
	 * @return A ResponseEntity object containing a list of `CategoryResponse`
	 *         objects and the HTTP status code. The status code will be: * 200 OK -
	 *         if categories are retrieved successfully. * 401 Unauthorized - if an
	 *         unsupported API version is specified.
	 * @throws Exception If an unexpected error occurs during category retrieval.
	 */
	@Operation(description = "Fetches category based on Id from the database", summary = "Fetches a Category by Id")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Ok", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@GetMapping("/getbyid")
	public ResponseEntity<ApiResponse<CategoryResponse>> fetchCategoryById(@RequestParam long categoryId,
			@PathVariable String version) {
		if (version.equals("v1"))
			return categoryService.fetchCategoryById(categoryId);

		throw new UnauthorizedVersionException();
	}

	@Operation(description = "Category icon url and a Alternative icon url is added to a Category", summary = "Updates the Category icons")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400")})
	@PatchMapping(value = "/uploadIcon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadIcon(@PathVariable String version,
			@RequestParam("iconfile") MultipartFile iconfile,@RequestParam("alternativeIconfile") MultipartFile alternativeIconfile, @RequestParam long categoryId) {
		if (version.equals("v1"))
			return categoryService.uploadIcon(iconfile,alternativeIconfile, categoryId);
		throw new UnauthorizedVersionException();
	}

	@Operation(description = "Assigns courses to a Category", summary = "Updates associated courses")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "OK", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400")})
	@PatchMapping(value = "/assigncourses")
	public ResponseEntity<ApiResponse<Category>> assignCoursesToCategory(@PathVariable String version,
			@RequestParam long categoryId, @RequestBody List<Long> courseIds) {
		if (version.equals("v1"))
			return categoryService.assignCoursesToCategory(categoryId, courseIds);

		throw new UnauthorizedVersionException("Unauthorized Version");

	}

	@GetMapping("/getCategory")
	public ResponseEntity<ApiResponse<List<CategoryFormResponse>>> fetchAllCategory(@PathVariable String version) {
		if (version.equals("v1"))
			return categoryService.fetchAllCategoryAndSubCategory();

		throw new UnauthorizedVersionException();
	}
	
}
