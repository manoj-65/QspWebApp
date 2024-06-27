package com.alpha.qspiderrestapi.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.CityCourseBranchView;

public interface CityCourseBranchViewRepository extends JpaRepository<CityCourseBranchView, Long> {

	@Query(value = "select ccbv.branch_id,\r\n" + "ccbv.branch_image,\r\n" + "ccbv.display_name,\r\n"
			+ "ccbv.contacts,\r\n" + "ccbv.location,\r\n" + "ccbv.upcoming_batches, \r\n" + "ccbv.ongoing_batches,\r\n"
			+ "b.branch_type,\r\n" + "a.city ,\r\n" + "a.pincode,\r\n" + "a.state,\r\n" + "a.street\r\n"
			+ "from city_course_branch_view ccbv, \r\n" + "address a, \r\n" + "branch b \r\n"
			+ "where a.city = ccbv.city\r\n" + "group by a.city, a.pincode, a.state, a.street,\r\n"
			+ "ccbv.branch_id,\r\n" + "ccbv.branch_image,\r\n" + "ccbv.display_name,\r\n" + "ccbv.contacts,\r\n"
			+ "ccbv.location,\r\n" + "ccbv.upcoming_batches,\r\n" + "b.branch_type,\r\n" + "ccbv.ongoing_batches;"
			+ "", nativeQuery = true)
	List<Map<String, Object>> findAllByCityName();
}
