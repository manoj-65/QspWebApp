package com.alpha.qspiderrestapi.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.CityCourseBranchView;

public interface CityCourseBranchViewRepository extends JpaRepository<CityCourseBranchView, Long> {

	@Query(value = "select ccbv.branch_id,ccbv.branch_image,ccbv.display_name,ccbv.contacts,ccbv.location,ccbv.upcoming_batches,\r\n"
			+ "ccbv.ongoing_batches,b.branch_type,a.city from city_course_branch_view ccbv, address a, branch b where a.city = ccbv.city\r\n"
			+ "group by a.city, ccbv.branch_id, ccbv.branch_image, ccbv.display_name, ccbv.contacts,\r\n"
			+ "ccbv.location, ccbv.upcoming_batches,b.branch_type,ccbv.ongoing_batches;\r\n" + "\r\n"
			+ "", nativeQuery = true)
	List<Map<String, Object>> findAllByCityName();
}
