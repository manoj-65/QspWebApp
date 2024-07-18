package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.qspiderrestapi.entity.SubCategory;

/**
 * Repository interface for SubCategory entities. This interface extends
 * JpaRepository to provide CRUD operations and custom query methods for
 * SubCategory entities.
 */
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

	/**
	 * Finds the sub-category ID of a SubCategory entity by its sub-category ID.
	 * 
	 * @param subCategoryId the ID of the sub-category to find.
	 * @return the sub-category ID if it exists, otherwise null.
	 */
	@Query(value = "Select s.subCategoryId From SubCategory s Where s.subCategoryId = :subCategoryId")
	Long findBySubCategoryId(@Param("subCategoryId") long subCategoryId);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO sub_category_course(sub_category_id, course_id) VALUES (:subCategoryId,:courseId)", nativeQuery = true)
	void assignCourseToSubCategory(long subCategoryId, long courseId);

	@Query(value = "Select sub_category_id From sub_category_course Where course_id = :courseId AND sub_category_id = :subCategoryId", nativeQuery = true)
	Long findBySubCategoryId(long subCategoryId, long courseId);

	@Transactional
	@Modifying
	@Query(value = "Delete from sub_category_course where sub_category_id = :subCategoryId and course_id IN (:courseIds)", nativeQuery = true)
	void removeCourseFromSubCategory(@Param("subCategoryId") Long subCategoryId,
			@Param("courseIds") List<Long> courseIds);

}
