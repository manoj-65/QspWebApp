package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.repository.SubCategoryRepository;

/**
 * Implementation of the SubCategoryDao interface. Provides methods to interact
 * with the SubCategoryRepository.
 * 
 * @see SubCategoryRepository
 */
@Repository
public class SubCategoryDaoImpl implements SubCategoryDao {

	@Autowired
	SubCategoryRepository subCategoryRepository;

	/**
	 * Saves a SubCategory entity.
	 *
	 * @param subCategory the SubCategory entity to be saved.
	 * @return the saved SubCategory entity.
	 */
	@Override
	public SubCategory saveSubCategory(SubCategory subCategory) {
		return subCategoryRepository.save(subCategory);
	}

	/**
	 * Fetches a SubCategory entity by its ID.
	 *
	 * @param subCategoryId the ID of the SubCategory to fetch.
	 * @return an Optional containing the fetched SubCategory, or empty if not
	 *         found.
	 */
	@Override
	public Optional<SubCategory> fetchSubCategoryById(long subCategoryId) {
		return subCategoryRepository.findById(subCategoryId);
	}

	/**
	 * Fetches all SubCategory entities.
	 *
	 * @return a List of all SubCategory entities.
	 */
	@Override
	public List<SubCategory> fetchAllSubCategories() {
		return subCategoryRepository.findAll();
	}

	/**
	 * Deletes a SubCategory entity by its ID.
	 *
	 * @param subCategoryId the ID of the SubCategory to delete.
	 */
	@Override
	public void deleteSubCategory(long subCategoryId) {
		subCategoryRepository.deleteById(subCategoryId);
	}

	/**
	 * Checks if a SubCategory entity is present by its ID.
	 *
	 * @param subCategoryId the ID of the SubCategory to check.
	 * @return true if the SubCategory entity is present, false otherwise.
	 */
	@Override
	public boolean isSubCategoryPresent(long subcategoryId) {
		return subCategoryRepository.findBySubCategoryId(subcategoryId) != null;
	}

	@Override
	public void assignCourseToSubCategory(long subCategoryId, long courseId) {
		subCategoryRepository.assignCourseToSubCategory(subCategoryId, courseId);
	}

	@Override
	public boolean isCourseIdPresent(long subCategoryId, long courseId) {
		if (subCategoryRepository.findBySubCategoryId(subCategoryId, courseId) == null)
			return false;
		return true;
	}

	@Override
	public void removeCourseFromSubCategory(Long subCategoryId, List<Long> courseIds) {
		subCategoryRepository.removeCourseFromSubCategory(subCategoryId, courseIds);
	}

	@Override
	public void deleteAllSubCategory() {
		subCategoryRepository.deleteAll();
	}

}
