package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.dto.BranchCourseDto;
import com.alpha.qspiderrestapi.entity.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

	@Query(value = "Select b.branchId From Branch b Where b.branchId = :branchId")
	Long findByBranchId(@Param("branchId") long branchId);

//	@Query("SELECT new com.alpha.qspiderrestapi.dto.BranchCourseDto(a.city, cii.cityImageUrl, COUNT(b.branchId)) "
//			+ "FROM Address a, City cii, Branch b "
//			+ "where a.city = cii.city_name AND a.address_id = b.address_id GROUP BY a.city, cii.city_image_url")
//	List<BranchCourseDto> findAllBranchDto();

}
