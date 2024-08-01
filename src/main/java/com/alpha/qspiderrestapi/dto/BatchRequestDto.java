package com.alpha.qspiderrestapi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BatchRequestDto {

	private String batchTitle;
	private String trainerName;
	private LocalDate startingDate;
	private LocalDate endingDate;
	private LocalTime startingTime;
	private LocalTime endingTime;
	private int extendingDays;
}
