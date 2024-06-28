package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.FaqDao;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.FaqType;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.repository.FaqRepository;

@Repository
public class FaqDaoImpl implements FaqDao {

	@Autowired
	private FaqRepository faqRepository;

	@Override
	public List<Faq> saveFaq(List<Faq> faqs) {
		return faqRepository.saveAll(faqs);
	}

	@Override
	public Optional<Faq> fetchFaqById(long faqId) {
		return faqRepository.findById(faqId);
	}

	@Override
	public List<Faq> fetchAllFaqs(Organization organization) {
		return faqRepository.findByOrganizationTypeAndFaqType(organization, FaqType.ORGANIZATION);
	}

	@Override
	public void deleteFaq(long faqId) {
		faqRepository.deleteById(faqId);
	}

	@Override
	public long isFaqPresent(long faqId) {
		return faqRepository.findByFaqId(faqId);
	}

}
