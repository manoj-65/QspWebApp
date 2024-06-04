package com.alpha.qspiderrestapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dao.BranchDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.BranchService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchDao branchDao;

	@Autowired
	private AWSS3Service awss3Service;

	@Override
	public ResponseEntity<ApiResponse<Branch>> saveBranch(Branch branch) {
		log.info("Saving branch: {}", branch);
		branch.setBranchFaqs(
				branch.getBranchFaqs().stream().peek((faqs) -> faqs.setBranch(branch)).collect(Collectors.toList()));
		 log.info("Branch saved successfully: {}", branch);
		return ResponseUtil.getCreated(branchDao.saveBranch(branch));
	}

	@Override

	public ResponseEntity<ApiResponse<String>> uploadImagesToGallery(List<MultipartFile> files, long branchId) {
		log.info("Entered uploadImageToGallery");
		String folder = "BRANCH/";
		Branch branch = branchDao.fetchBranchById(branchId).orElseThrow(() -> {
			log.error("Branch with given Id: {} " + branchId + " Not found");
			return new IdNotFoundException("Branch With the Given Id Not Found");
		});
		folder += branch.getBranchTitle();
		for (MultipartFile file : files) {
			String iconUrl = awss3Service.uploadFile(file, folder);
			if (!(iconUrl.isEmpty())) {
				log.info("Icon uploaded successfully to S3: {}", iconUrl);
				List<String> gallery = branch.getGallery();
				gallery.add(iconUrl);
				branch.setGallery(gallery);
				branchDao.saveBranch(branch);
			} else
				log.error("Icon cant be uploaded due to admin restriction");
			throw new NullPointerException("Icon can't be Upload Due the Admin restriction");
		}
		return ResponseUtil.getCreated("Icon Uplodaed!!");
	}

	public ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long branchId) {
		log.info("Uploading icon for branch ID: {}", branchId);
		String folder = "BRANCH/";
		Optional<Branch> optionalBranch = branchDao.fetchBranchById(branchId);
		if (optionalBranch.isPresent()) {
			folder += optionalBranch.get().getBranchTitle();
			String iconUrl = awss3Service.uploadFile(file, folder);
			if (!iconUrl.isEmpty()) {
				log.info("File successfully uploaded to sw3: {}" + iconUrl);
				optionalBranch.get().setBranchImage(iconUrl);
				branchDao.saveBranch(optionalBranch.get());
				return ResponseUtil.getCreated(iconUrl);
			}
			log.error("Icon cant be uploaded due to admin restriction");
			throw new NullPointerException("Icon can't be Upload Due the Admin restriction");
		}
		log.error("Branch not found with ID: {}", branchId);
		throw new IdNotFoundException("Branch With the Given Id Not Found");
	}

}
