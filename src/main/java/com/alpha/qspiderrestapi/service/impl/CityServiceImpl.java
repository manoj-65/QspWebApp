package com.alpha.qspiderrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dao.CityDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.City;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.service.CityService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
	private CityDao cityDao;

	@Autowired
	private AWSS3ServiceImpl awss3ServiceImpl;

	@Override
	public ResponseEntity<ApiResponse<City>> saveCity(MultipartFile cityIcon, MultipartFile cityImage,
			String cityName) {
		City city = new City();
		String iconFolder = "CITY/ICON/";
		String imageFolder = "CITY/IMAGE/";
		if (cityName != null) {
			iconFolder += cityName;
			imageFolder += cityName;
			String iconUrl = awss3ServiceImpl.uploadFile(cityIcon, iconFolder);
			String imageUrl = awss3ServiceImpl.uploadFile(cityImage, imageFolder);
			if (!iconUrl.isEmpty()) {
				if (!imageUrl.isEmpty()) {
					city.setCityName(cityName);
					city.setCityIconUrl(iconUrl);
					city.setCityImageUrl(imageUrl);
					city = cityDao.save(city);
					return ResponseUtil.getCreated(city);
				} else
					throw new NullPointerException("CityImage can't be Uploaded Due the Admin restriction");
			} else
				throw new NullPointerException("CityIcon can't be Uploaded Due the Admin restriction");
		} else
			throw new IdNotFoundException("No City found with the given name : " + cityName);
	}

}
