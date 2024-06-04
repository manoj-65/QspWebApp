package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.FaqDao;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.repository.FaqRepository;

@Repository
public class FaqDaoImpl implements FaqDao {

	@Autowired
	private FaqRepository faqRepository;

	@Override
	public Faq saveFaq(Faq faq) {
		return faqRepository.save(faq);
	}

	@Override
	public Optional<Faq> fetchFaqById(long faqId) {
		return faqRepository.findById(faqId);
	}

	@Override
	public List<Faq> fetchAllFaqs() {
		return faqRepository.findAll();
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
