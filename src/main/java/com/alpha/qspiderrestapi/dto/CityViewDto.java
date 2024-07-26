package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CityViewDto {

	private String cityName;
	private List<BranchDto> branches;
}
