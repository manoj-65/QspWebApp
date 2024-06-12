package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CityDto {
	
	private String cityIcon;
	private String cityName;
	private List<CourseDto> courses;
}
