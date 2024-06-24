package com.alpha.qspiderrestapi.service.impl;

import com.alpha.qspiderrestapi.entity.Enquiry;
import com.alpha.qspiderrestapi.service.EmailData;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EnquiryEmailData implements EmailData {

	private final Enquiry enquiry;

	@Override
	public String getSubject() {
		return "Enquiry Received - " + enquiry.getEnquiryId() + "Enquiry Type: " + enquiry.getEnquiryType();
	}

	@Override
	public String getContent() {
		StringBuilder content = new StringBuilder();
		content.append("Enquiry ID: ").append(enquiry.getEnquiryId()).append("\n");
		content.append(enquiry.toString());
		return content.toString();
	}
}
