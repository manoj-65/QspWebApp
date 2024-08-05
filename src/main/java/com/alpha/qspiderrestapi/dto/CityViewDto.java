package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CityViewDto {

	private String cityName;
	private long jspiders;
	private long qspiders;
	private long pyspiders;
	private long prospiders;
	private List<BranchDto> branches;

}
