package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFormResponse {

	private long categoryId;
	private String categoryName;

	private List<SubCategoryFormResponse> subCategoryResponse;
}
