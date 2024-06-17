package com.alpha.qspiderrestapi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchById_BatchDto {

	private long batchId;
	private String batchName;
	private String trainerName;
	private LocalDate startingDate;
	private LocalDate endingDate;
	private LocalTime startingTime;
	private LocalTime endingTime;
	private String batchType;
}
