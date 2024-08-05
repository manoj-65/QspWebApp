package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.qspiderrestapi.entity.Category;

/**
 * Repository interface for Category entities. This interface extends
 * JpaRepository to provide CRUD operations and custom query methods for
 * Category entities.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

	/**
	 * Finds the category ID of a Category entity by its category ID.
	 * 
	 * @param categoryId the ID of the category to find.
	 * @return the category ID if it exists, otherwise null.
	 */
	@Query(value = "Select c.categoryId From Category c Where c.categoryId = :categoryId")
	Long findByCategoryId(@Param("categoryId") long categoryId);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO category_course(category_id, course_id) VALUES (:categoryId,:courseId)", nativeQuery = true)
	void assignCourseToCategory(long categoryId, long courseId);

	@Query(value = "Select category_id From category_course Where category_id = :categoryId AND course_id = :courseId", nativeQuery = true)
	Long findByCourseId(@Param("categoryId") long categoryId, @Param("courseId") long courseId);

	List<Category> findAllByOrderByCategoryIdAsc();

	@Transactional
	@Modifying
	@Query(value = "delete from category_course where course_id IN (:courseIds) and category_id = :categoryId", nativeQuery = true)
	int removeCourseById(@Param("courseIds") List<Long> courseIds, @Param("categoryId") long categoryId);

//	void deleteCategoryWithoutUnmap(Long categoryId);

}
