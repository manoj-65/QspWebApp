package com.alpha.qspiderrestapi.modelmapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.CategoryFormResponse;
import com.alpha.qspiderrestapi.dto.CategoryResponse;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.util.CourseUtil;
import com.alpha.qspiderrestapi.util.WeightageUtil;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Component
public class CategoryMapper {

	@Autowired
	private WeightageUtil weightageUtil;
	
	@Autowired
	private SubCategoryMapper subCategoryMapper;
	
	@Autowired
	private CourseMapper courseMapper;
	
	@Autowired
	private CourseUtil courseUtil;
	
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
	public CategoryResponse mapToCategoryDto(Category category,String hostname,boolean isOnline) {
		if(isOnline) {
			category.setCourses(courseUtil.filterForOnline(category.getCourses()));
		}
		return CategoryResponse.builder().courseId(category.getCategoryId())
				.icon(category.getCategoryIcon())
				.alternativeIcon(category.getCategoryAlternativeIcon())
				.title(category.getCategoryTitle())
				.subCourse(subCategoryMapper.mapToSubCategoryResponseList(category.getSubCategories(),hostname,category.getCategoryId(),isOnline))
				.courseResponse(courseMapper.mapToCourseResponseList(weightageUtil.getSortedCourseOfCategory(category.getCourses(), hostname, category.getCategoryId()),category.getCategoryId()))
				.build();
	}

	public CategoryFormResponse mapToCategoryFormResponse(Category category) {
		return CategoryFormResponse.builder().categoryId(category.getCategoryId())
				.categoryName(category.getCategoryTitle())
				.subCategoryResponse(subCategoryMapper.mapToListSubCategoryFormResponse(category.getSubCategories()))
				.build();
	}

}
