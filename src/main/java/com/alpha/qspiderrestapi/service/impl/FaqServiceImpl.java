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
import com.alpha.qspiderrestapi.exception.DomainMismatchException;
import com.alpha.qspiderrestapi.exception.FaqNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidOrganisationTypeException;
import com.alpha.qspiderrestapi.service.FaqService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

@Service
public class FaqServiceImpl implements FaqService {

	@Autowired
	private FaqDao faqDao;

	@Value("${organization.qsp}")
	private String qspDomainName;

	@Value("${organization.jsp}")
	private String jspDomainName;

	@Value("${organization.pysp}")
	private String pyspDomainName;

	@Value("${organization.bsp}")
	private String prospDomainName;

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
	public ResponseEntity<ApiResponse<List<Faq>>> fetchAllFaqs(String origin) {
		Organization organization = null;
		if(qspDomainName.equals(origin)) 
			organization = Organization.QSP;
		else if(jspDomainName.equals(origin))
			organization = Organization.JSP;
		else if(pyspDomainName.equals(origin))
			organization = Organization.PYSP;
		else if(prospDomainName.equals(origin))
			organization = Organization.PROSP;
		else
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
	
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
