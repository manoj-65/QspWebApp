package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.qspiderrestapi.entity.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

}
