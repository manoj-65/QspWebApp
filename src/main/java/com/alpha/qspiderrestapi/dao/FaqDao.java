package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Faq;

public interface FaqDao {

	List<Faq> saveFaq(List<Faq> faqs);

	Optional<Faq> fetchFaqById(long faqId);

	List<Faq> fetchAllFaqs();

	void deleteFaq(long faqId);

	long isFaqPresent(long faqId);
}
