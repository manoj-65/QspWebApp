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
import com.alpha.qspiderrestapi.dto.CityCourseBranchView;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.repository.CityRepo;
import com.alpha.qspiderrestapi.service.BranchService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/branches")
public class BranchController {

	@Autowired
	private BranchService branchService;

	@Autowired
	private CityRepo repo;

	@PostMapping
	public ResponseEntity<ApiResponse<Branch>> saveBranch(@RequestBody Branch branch, @PathVariable String version) {

		if (version.equals("v1"))
			return branchService.saveBranch(branch);

		throw new UnauthorizedVersionException();

	}

	@PatchMapping(value = "/uploadImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadImagesToGallery(@PathVariable String version,
			@RequestParam("files") List<MultipartFile> files, @RequestParam long branchId) {
		if (version.equals("v1"))
			return branchService.uploadImagesToGallery(files, branchId);
		throw new UnauthorizedVersionException();
	}

	@PatchMapping(value = "/uploadIcon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<String>> uploadIcon(@PathVariable String version,
			@RequestParam("file") MultipartFile file, @RequestParam long branchId) {
		if (version.equals("v1"))
			return branchService.uploadIcon(file, branchId);
		throw new UnauthorizedVersionException();	
	}

	@GetMapping
	public List<CityCourseBranchView> fetchByBranch(@PathVariable String version) {
		if (version.equals("v1"))
			return repo.findAll();

		throw new UnauthorizedVersionException();
	}

}
