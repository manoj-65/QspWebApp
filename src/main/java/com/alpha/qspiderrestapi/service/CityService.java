package com.alpha.qspiderrestapi.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.City;

public interface CityService {

	ResponseEntity<ApiResponse<City>> saveCity(MultipartFile cityIcon, MultipartFile cityImage, String cityName);

	ResponseEntity<ApiResponse<City>> updateCity(MultipartFile cityIcon, MultipartFile cityImage, String cityName,
			String newCityName);

}
