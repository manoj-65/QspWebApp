package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import com.alpha.qspiderrestapi.dto.BranchById_BatchDto;
import com.alpha.qspiderrestapi.entity.Batch;

public class BranchById_BatchDtoMapper {

	public static List<BranchById_BatchDto> mapToBranchById_BatchDto(List<Batch> batches) {
		List<BranchById_BatchDto> batchDtos = new ArrayList<BranchById_BatchDto>();
		batches.forEach(batch->{
			batchDtos.add((BranchById_BatchDto.builder()
							   .batchId(batch.getBatchId())
							   .batchName(batch.getCourse().getCourseName())
							   .trainerName(batch.getTrainerName())
							   .batchType("Weekdays")
							   .startingDate(batch.getStartingDate())
							   .endingDate(batch.getEndingDate())
							   .startingTime(batch.getStartingTime())
							   .endingTime(batch.getEndingTime())
							   .build()));
		});
		return batchDtos;
	}

}
