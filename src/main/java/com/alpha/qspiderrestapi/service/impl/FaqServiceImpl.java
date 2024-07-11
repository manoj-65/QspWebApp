package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.FaqDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.FaqDto;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.FaqType;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.FaqNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidOrganisationTypeException;
import com.alpha.qspiderrestapi.service.FaqService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

@Service
public class FaqServiceImpl implements FaqService {

	@Autowired
	private FaqDao faqDao;

	@Value("organisatin.qsp")
	private String qspDomainName;

	@Override
	public ResponseEntity<ApiResponse<List<Faq>>> saveFaq(List<FaqDto> faqDtos, Organization organizationType) {

		List<Faq> faqs = new ArrayList<Faq>();

		faqDtos.stream().forEach(faqDto -> {
			Faq faq = new Faq();
			faq.setQuestion(faqDto.getQuestion());
			faq.setAnswer(faqDto.getAnswer());
			faq.setFaqType(FaqType.ORGANIZATION);
			faq.setOrganizationType(organizationType);
			faqs.add(faq);
		});

		return ResponseUtil.getCreated(faqDao.saveFaq(faqs));
	}

	@Override
	public ResponseEntity<ApiResponse<List<Faq>>> fetchAllFaqs(Organization organization) {

		List<Faq> faQs = faqDao.fetchAllFaqs(organization);
		if (faQs != null) {
			if (!faQs.isEmpty()) {
				return ResponseUtil.getOk(faQs);
			}
			throw new FaqNotFoundException("No Faqs found for this organisation");
		}

		throw new InvalidOrganisationTypeException("Organisation type not found!!");

	}

}
