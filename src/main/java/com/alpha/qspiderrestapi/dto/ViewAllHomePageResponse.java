package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewAllHomePageResponse {

	private String countryName;
	private Long ctQspiders;
	private Long ctJspiders;
	private Long ctPyspiders;
	private Long ctProspiders;
	private List<CityViewDto> city;

}
