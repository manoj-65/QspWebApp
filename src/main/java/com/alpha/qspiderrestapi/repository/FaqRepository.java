package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.FaqType;
import com.alpha.qspiderrestapi.entity.enums.Organization;

public interface FaqRepository extends JpaRepository<Faq, Long> {

	@Query(value = "Select f.faqId From Faq f Where f.faqId = :faqId")
	Long findByFaqId(@Param("faqId") long faqId);

	List<Faq> findByOrganizationTypeAndFaqType(Organization organization,FaqType faqType);
}
