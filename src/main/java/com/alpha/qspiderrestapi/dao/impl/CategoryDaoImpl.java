package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.repository.CategoryRepository;

@Repository
public class CategoryDaoImpl implements CategoryDao {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Optional<Category> fetchCategoryById(long categoryId) {
		return categoryRepository.findById(categoryId);
	}

	@Override
	public List<Category> fetchAllCategories() {
		return categoryRepository.findAllByOrderByCategoryIdAsc();
	}

	@Override
	public void deleteCategory(long categoryId) {
		categoryRepository.deleteById(categoryId);
	}

	@Override
	public boolean isCategoryPresent(long categoryId) {
		return categoryRepository.findByCategoryId(categoryId) != null;
	}

	@Override
	public void assignCourseToCategory(long categoryId, long courseId) {
		categoryRepository.assignCourseToCategory(categoryId, courseId);
	}

	@Override
	public boolean isCourseIdPresent(long categoryId, long courseId) {
		if (categoryRepository.findByCourseId(categoryId, courseId) == null)
			return false;
		return true;
	}

}
