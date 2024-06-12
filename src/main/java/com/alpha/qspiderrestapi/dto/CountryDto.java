package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CountryDto {

	private String countryIcon;
	private String countryName;
	private List<CityDto> cities;
}
