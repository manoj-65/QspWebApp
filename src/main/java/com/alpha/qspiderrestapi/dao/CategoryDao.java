package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Category;

public interface CategoryDao {

	Category saveCategory(Category category);

	Optional<Category> fetchCategoryById(long categoryId);

	List<Category> fetchAllCategories();

	void deleteCategory(long categoryId);

	boolean isCategoryPresent(long categoryId);

	void assignCourseToCategory(long categoryId, long courseId);

	boolean isCourseIdPresent(long categoryId, long courseId);

	void removeCourseFromCategory(Long courseId, Long categoryId);
}
