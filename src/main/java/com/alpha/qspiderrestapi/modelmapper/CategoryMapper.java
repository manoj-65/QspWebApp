package com.alpha.qspiderrestapi.modelmapper;

import com.alpha.qspiderrestapi.dto.CategoryFormResponse;
import com.alpha.qspiderrestapi.dto.CategoryResponse;
import com.alpha.qspiderrestapi.entity.Category;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class CategoryMapper {

	/**
	 * Converts a Category entity to a CategoryResponse DTO.
	 *
	 * This method transforms a `Category` object into a `CategoryResponse` Data
	 * Transfer Object (DTO). The DTO contains relevant information about the
	 * category, excluding potentially sensitive data.
	 * 
	 * @param category The Category entity to be converted.
	 * @return A CategoryResponse DTO containing the mapped data.
	 */
	public static CategoryResponse mapToCategoryDto(Category category) {

		return CategoryResponse.builder().courseId(category.getCategoryId()).icon(category.getCategoryIcon())
				.title(category.getCategoryTitle())
				.subCourse(SubCategoryMapper.mapToSubCategoryResponseList(category.getSubCategories()))
				.courseResponse(CourseMapper.mapToCourseResponseList(category.getCourses().stream()
						.sorted((a, b) -> (int) a.getCourseId() - (int) b.getCourseId()).toList()))
				.build();
	}

	public static CategoryFormResponse mapToCategoryFormResponse(Category category) {
		return CategoryFormResponse.builder().categoryId(category.getCategoryId())
				.categoryName(category.getCategoryTitle())
				.subCategoryResponse(SubCategoryMapper.mapToListSubCategoryFormResponse(category.getSubCategories()))
				.build();
	}

}
