package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.ViewAllHomePage;

public interface ViewAllHomePageRepository extends JpaRepository<ViewAllHomePage, Long> {

	@Query(value = "select v from ViewAllHomePage v where v.branchType = :organisation")
	List<ViewAllHomePage> findByOrganizationType(String organisation);
}
