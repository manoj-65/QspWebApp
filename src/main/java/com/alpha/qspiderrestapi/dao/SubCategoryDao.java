package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.SubCategory;

public interface SubCategoryDao {

	SubCategory saveSubCategory(SubCategory subCategory);

	Optional<SubCategory> fetchSubCategoryById(long subCategoryId);

	List<SubCategory> fetchAllSubCategories();

	void deleteSubCategory(long subCategoryId);

	boolean isSubCategoryPresent(long subcategoryId);

	void assignCourseToSubCategory(long subCategoryId, long courseId);

	boolean isCourseIdPresent(long subCategoryId, long courseId);

	void removeCourseFromSubCategory(Long subCategoryId, List<Long> courseIds);

}
