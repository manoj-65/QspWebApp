package com.alpha.qspiderrestapi.modelmapper;

import com.alpha.qspiderrestapi.dto.BranchByIdDto;
import com.alpha.qspiderrestapi.entity.Branch;

public class BranchById_Mapper {
	
	public static BranchByIdDto mapToBranchByIdDto(Branch branch) {
		return BranchByIdDto.builder()
							.name(branch.getDisplayName())
							.branchImage(branch.getBranchImage())
							.branchGallery(branch.getGallery())
							.address(branch.getBranchAddress())
							.faqs(branch.getBranchFaqs())
							.courses(BranchById_CourseDtoMapper.mapToBranchById_CourseDto(branch.getBatches()))
							.batches(BranchById_BatchDtoMapper.mapToBranchById_BatchDto(branch.getBatches()))
							.build();
	}

}
