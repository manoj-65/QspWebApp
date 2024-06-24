package com.alpha.qspiderrestapi.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.EnquiryDao;
import com.alpha.qspiderrestapi.entity.Enquiry;
import com.alpha.qspiderrestapi.repository.EnquiryRepository;

@Repository
public class EnquiryDaoImpl implements EnquiryDao {

	@Autowired
	EnquiryRepository enquiryRepository;

	@Override
	public Enquiry save(Enquiry enquiry) {
		return enquiryRepository.save(enquiry);
	}

}
