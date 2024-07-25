package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.SubCategoryFormResponse;
import com.alpha.qspiderrestapi.dto.SubCategoryResponse;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.util.CourseUtil;
import com.alpha.qspiderrestapi.util.WeightageUtil;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Component
public class SubCategoryMapper {

	@Autowired
	private WeightageUtil weightageUtil;
	
	@Autowired
	private SubCourseMapper subCourseMapper;
	
	@Autowired
	private CourseUtil courseUtil;
	
	/**
	 * Converts a SubCategory entity to a SubCategoryResponse DTO.
	 *
	 * This method transforms a `SubCategory` object into a `SubCategoryResponse`
	 * Data Transfer Object (DTO). The DTO contains relevant information about the
	 * sub-category, excluding potentially sensitive data.
	 * 
	 * @param subCategory The SubCategory entity to be converted.
	 * @param subCategoryId 
	 * @param hostname 
	 * @return A SubCategoryResponse DTO containing the mapped data.
	 */
	public SubCategoryResponse mapToSubCategoryResponse(SubCategory subCategory, String hostname,boolean isOnline) {
		if(isOnline) {
			subCategory.setCourses(courseUtil.filterForOnline(subCategory.getCourses()));
		}
		return SubCategoryResponse.builder().subCourseId(subCategory.getSubCategoryId())
				.icon(subCategory.getSubCategoryIcon()).title(subCategory.getSubCategoryTitle())
				.subCourseResponse(subCourseMapper.mapToSubCourseResponseList(weightageUtil.getSortedCourseOfSubCategory(subCategory.getCourses(), hostname, subCategory.getSubCategoryId()),subCategory.getSubCategoryId()))
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
	public List<SubCategoryResponse> mapToSubCategoryResponseList(List<SubCategory> subCategories,String hostname,long categoryId,boolean isOnline) {
		subCategories = weightageUtil.getSortedSubCategory(subCategories, hostname, categoryId);
		List<SubCategoryResponse> responseList = new ArrayList<SubCategoryResponse>();

		subCategories.forEach(response -> responseList.add(mapToSubCategoryResponse(response,hostname,isOnline)));

		return responseList;

	}

	public List<SubCategoryFormResponse> mapToListSubCategoryFormResponse(List<SubCategory> subCategories) {

		List<SubCategoryFormResponse> subCategoryFormList = new ArrayList<SubCategoryFormResponse>();
		subCategories.forEach(response -> subCategoryFormList.add(mapToSubCategoryFormResponse(response)));
		return subCategoryFormList;
	}

	private SubCategoryFormResponse mapToSubCategoryFormResponse(SubCategory response) {
		return SubCategoryFormResponse.builder().subCategoryId(response.getSubCategoryId())
				.subCategoryName(response.getSubCategoryTitle()).build();
	}

}
