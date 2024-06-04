package com.alpha.qspiderrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryFormResponse {

	private long subCategoryId;
	private String subCategoryName;
}
