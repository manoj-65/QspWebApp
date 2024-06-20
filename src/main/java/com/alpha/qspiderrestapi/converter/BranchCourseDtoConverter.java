//package com.alpha.qspiderrestapi.converter;
//
//import org.springframework.core.convert.converter.Converter;
//
//import com.alpha.qspiderrestapi.dto.BranchCourseDto;
//
//@Converter
//public class BranchCourseDtoConverter implements Converter<Object[], BranchCourseDto> {
//
//	@Override
//	public BranchCourseDto convert(Object[] source) {
//		if (source == null || source.length != 3) {
//			return null; // Handle invalid data or different result set size
//		}
//		BranchCourseDto dto = new BranchCourseDto();
//		dto.setCity((String) source[0]);
//		dto.setCityImageUrl((String) source[1]);
//		dto.setBranchCount((Long) source[2]); // Assuming branch count is a Long
//		return dto;
//	}
//
//}
