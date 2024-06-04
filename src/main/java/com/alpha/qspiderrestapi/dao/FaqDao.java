package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Faq;

public interface FaqDao {

	Faq saveFaq(Faq faq);

	Optional<Faq> fetchFaqById(long faqId);

	List<Faq> fetchAllFaqs();

	void deleteFaq(long faqId);

	long isFaqPresent(long faqId);
}
