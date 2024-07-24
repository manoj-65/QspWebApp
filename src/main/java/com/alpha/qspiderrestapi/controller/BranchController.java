package com.alpha.qspiderrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.BranchByIdDto;
import com.alpha.qspiderrestapi.dto.BranchFileRequestDto;
import com.alpha.qspiderrestapi.dto.CountryDto;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.BranchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/branches")
public class BranchController {

	@Autowired
	private BranchService branchService;

	@Operation(description = "A Branch is saved into the database", summary = "Saves a Branch")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401") })
	@PostMapping
	public ResponseEntity<ApiResponse<Branch>> saveBranch(@RequestBody Branch branch, @PathVariable String version) {
		if (version.equals("v1")) {
			return branchService.saveBranch(branch);
		}
		throw new UnauthorizedVersionException();

	}

	@Operation(description = "Branch Images urls are added to a branch", summary = "Updates the branch images")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400") })
	@PatchMapping(value = "/uploadImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadImagesToGallery(@PathVariable String version,
			@RequestParam("files") List<MultipartFile> files, @RequestParam long branchId) {
		if (version.equals("v1"))
			return branchService.uploadImagesToGallery(files, branchId);
		throw new UnauthorizedVersionException();
	}

	@Operation(description = "A Branch icon url is added to a branch", summary = "Updates the branch icon")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400") })
	@PatchMapping(value = "/uploadIcon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadIcon(@PathVariable String version,
			@RequestParam("file") MultipartFile file, @RequestParam long branchId) {
		if (version.equals("v1"))
			return branchService.uploadIcon(file, branchId);
		throw new UnauthorizedVersionException();
	}

	@GetMapping("/getAllBranches")
	public ResponseEntity<ApiResponse<List<CountryDto>>> fetchAll(@PathVariable String version,
			@RequestHeader("Origin") String domainName) {
		if (version.equals("v1"))
			return branchService.fetchAll(domainName);

		throw new UnauthorizedVersionException();
	}

	@GetMapping("/getbyid")
	public ResponseEntity<ApiResponse<BranchByIdDto>> fetchById(@PathVariable String version,
			@RequestParam long branchId, @RequestParam long courseId) {
		if (version.equals("v1"))
			return branchService.fetchById(branchId, courseId);

		throw new UnauthorizedVersionException();
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable String version, @RequestParam long branchId) {
		if (version.equals("v1"))
			return branchService.deleteById(branchId);

		throw new UnauthorizedVersionException();
	}

	@PatchMapping("/modifyLocationUrl")
	public ResponseEntity<ApiResponse<String>> updateBranchLocation(@PathVariable String version,
			@RequestParam long branchId, @RequestParam String locationUrl) {
		if (version.equals("v1"))
			return branchService.updateBranchLocation(branchId, locationUrl);

		throw new UnauthorizedVersionException();
	}

	@GetMapping("/findAll")
	public ResponseEntity<ApiResponse<List<Branch>>> findAll(@PathVariable String version) {
		if (version.equals("v1"))
			return branchService.findAll();
		throw new UnauthorizedVersionException("Given Version Build is Not Runing");
	}

	@PostMapping(value = "/uploadFileAndData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<Branch>> saveBranchAlongWithFile(@PathVariable String version,
			@ModelAttribute BranchFileRequestDto branchRequestDto) {
		if (version.equals("v1"))
			return branchService.saveBranchAlongWithFile(branchRequestDto);

		throw new UnauthorizedVersionException();

	}

}
