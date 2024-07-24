package com.alpha.qspiderrestapi.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BranchFileRequestDto {

	private String branch;
	private MultipartFile branchImage;
	private List<MultipartFile> branchGallery;
}
