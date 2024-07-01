package com.alpha.qspiderrestapi.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.CityCourseBranchView;

public interface CityCourseBranchViewRepository extends JpaRepository<CityCourseBranchView, Long> {

	@Query(value = "\r\n" + "	\r\n"
			+ "		select ccbv.branch_id, ccbv.branch_image, ccbv.display_name, ccbv.contacts, ccbv.location, ccbv.upcoming_batches, ccbv.ongoing_batches,\r\n"
			+ "			b.branch_type, a.city , a.pincode, a.state, a.street from city_course_branch_view ccbv, address a, branch b where a.city = ccbv.city and b.branch_id = b.address_id group by a.city, a.pincode, a.state, a.street,\r\n"
			+ "			ccbv.branch_id, ccbv.branch_image, ccbv.display_name, ccbv.contacts, ccbv.location, ccbv.upcoming_batches, b.branch_type, ccbv.ongoing_batches;\r\n"
			+ "			\r\n", nativeQuery = true)
	List<Map<String, Object>> findAllByCityName();
}
