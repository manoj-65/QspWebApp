package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.Weightage;

public interface WeightageRepository extends JpaRepository<Weightage, Long> {

	@Query(value = "SELECT * FROM weightage WHERE  sub_category_category_id = :categoryId ", nativeQuery = true)
	List<Weightage> findBySubCategoryCategoryId(long categoryId);

	@Query(value = "select MAX(jspiders) as max_jspider,\r\n" + "MAX(qspiders) as max_qspider, \r\n"
			+ "MAX(pyspiders) as max_pyspider, \r\n" + "MAX(bspiders) as max_bspider\r\n" + "from weightage\r\n"
			+ "	where id = 4", nativeQuery = true)
	List<Integer> fetchWeightById(long weightageId);

	@Query(value = "select * from weightage where category_id is not null", nativeQuery = true)
	List<Weightage> fetchAllByCategoryId();

	@Query(value = "SELECT * FROM weightage WHERE course_category_id = :categoryId ", nativeQuery = true)
	List<Weightage> findCourseOfCategoryWeightages(long categoryId);

	@Query(value = "SELECT * FROM weightage WHERE course_sub_category_id = :subCategoryId ", nativeQuery = true)
	List<Weightage> findCourseOfSubCategoryWeightages(Long subCategoryId);

}
