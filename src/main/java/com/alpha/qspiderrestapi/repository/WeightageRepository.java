package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.Weightage;

public interface WeightageRepository extends JpaRepository<Weightage, Long> {

	@Query(value = "SELECT * FROM weightage WHERE  sub_category_category_id = :categoryId ",nativeQuery = true)
	List<Weightage> findBySubCategoryCategoryId(long categoryId);

}
