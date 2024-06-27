package com.alpha.qspiderrestapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeightageDto {

	private long qspiders;
	private long jspiders;
	private long pyspiders;
	private long bspiders;
}
