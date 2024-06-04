package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import com.alpha.qspiderrestapi.dto.SubCategoryFormResponse;
import com.alpha.qspiderrestapi.dto.SubCategoryResponse;
import com.alpha.qspiderrestapi.entity.SubCategory;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubCategoryMapper {

	/**
	 * Converts a SubCategory entity to a SubCategoryResponse DTO.
	 *
	 * This method transforms a `SubCategory` object into a `SubCategoryResponse`
	 * Data Transfer Object (DTO). The DTO contains relevant information about the
	 * sub-category, excluding potentially sensitive data.
	 * 
	 * @param subCategory The SubCategory entity to be converted.
	 * @return A SubCategoryResponse DTO containing the mapped data.
	 */
	public static SubCategoryResponse mapToSubCategoryResponse(SubCategory subCategory) {
		return SubCategoryResponse.builder().subCourseId(subCategory.getSubCategoryId())
				.icon(subCategory.getSubCategoryIcon()).title(subCategory.getSubCategoryTitle())
				.subCourseResponse(SubCourseMapper.mapToSubCourseResponseList(subCategory.getCourses().stream()
						.sorted((a, b) -> (int) a.getCourseId() - (int) b.getCourseId()).toList()))
				.build();
	}

	/**
	 * Converts a list of SubCategory entities to a list of SubCategoryResponse
	 * DTOs.
	 *
	 * This method iterates over a list of `SubCategory` entities and transforms
	 * each one into a `SubCategoryResponse` DTO using the
	 * `mapToSubCategoryResponse` method. The resulting list contains DTOs
	 * representing all the sub-categories in the input list.
	 * 
	 * @param subCategories The list of SubCategory entities to be converted.
	 * @return A list of SubCategoryResponse DTOs containing the mapped data.
	 */
	public static List<SubCategoryResponse> mapToSubCategoryResponseList(List<SubCategory> subCategories) {

		List<SubCategoryResponse> responseList = new ArrayList<SubCategoryResponse>();

		subCategories.forEach(response -> responseList.add(mapToSubCategoryResponse(response)));

		return responseList;

	}

	public static List<SubCategoryFormResponse> mapToListSubCategoryFormResponse(List<SubCategory> subCategories) {

		List<SubCategoryFormResponse> subCategoryFormList = new ArrayList<SubCategoryFormResponse>();
		subCategories.forEach(response -> subCategoryFormList.add(mapToSubCategoryFormResponse(response)));
		return subCategoryFormList;
	}

	private static SubCategoryFormResponse mapToSubCategoryFormResponse(SubCategory response) {
		return SubCategoryFormResponse.builder().subCategoryId(response.getSubCategoryId())
				.subCategoryName(response.getSubCategoryTitle()).build();
	}

}
