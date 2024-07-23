package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CountryDto {

	private String countryIcon;
	private String countryName;
	private long ctQspiders;
	private long ctJspiders;
	private long ctPyspiders;
	private long ctProspiders;
	private List<CityDto> cities;
}
