package com.alpha.qspiderrestapi.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDashboardResponse {

	private long categoryId;
	private String categoryName;
	private String categoryAlternativeIcon;
	private List<CourseResponse> offline;
	private List<CourseResponse> online;
	private List<CourseResponse> selfpaced;
	private List<CourseResponse> experiential;
}
