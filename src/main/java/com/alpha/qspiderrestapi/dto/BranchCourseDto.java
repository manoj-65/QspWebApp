package com.alpha.qspiderrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchCourseDto {
	private String city;
	private String cityImageUrl;
	private int branchCount;

//    public BranchCourseDto(String city, String cityImageUrl, int branchCount) {
//        this.city = city;
//        this.cityImageUrl = cityImageUrl;
//        this.branchCount = branchCount;
//    }
}
