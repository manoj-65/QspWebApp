package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Branch;

public interface BranchService {

	ResponseEntity<ApiResponse<Branch>> saveBranch(Branch branch);

	ResponseEntity<ApiResponse<String>> uploadImagesToGallery(List<MultipartFile> files, long branchId);

	ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long branchId);
}
