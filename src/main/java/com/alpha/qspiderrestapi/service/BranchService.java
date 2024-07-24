package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.BranchByIdDto;
import com.alpha.qspiderrestapi.dto.BranchFileRequestDto;
import com.alpha.qspiderrestapi.dto.CountryDto;
import com.alpha.qspiderrestapi.entity.Branch;

public interface BranchService {

	ResponseEntity<ApiResponse<Branch>> saveBranch(Branch branch);

	ResponseEntity<ApiResponse<String>> uploadImagesToGallery(List<MultipartFile> files, long branchId);

	ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long branchId);

	ResponseEntity<ApiResponse<List<CountryDto>>> fetchAll(String domainName);

	ResponseEntity<ApiResponse<BranchByIdDto>> fetchById(long branchId, long courseId);

	ResponseEntity<ApiResponse<String>> deleteById(long branchId);

	ResponseEntity<ApiResponse<List<Branch>>> findAll();

	ResponseEntity<ApiResponse<String>> updateBranchLocation(long branchId, String location);

	ResponseEntity<ApiResponse<Branch>> saveBranchAlongWithFile(BranchFileRequestDto branchRequestDto);
}
