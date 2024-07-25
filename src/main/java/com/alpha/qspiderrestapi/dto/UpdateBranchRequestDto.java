package com.alpha.qspiderrestapi.dto;

import java.util.List;

import com.alpha.qspiderrestapi.entity.Address;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.Organization;

import lombok.Data;

@Data
public class UpdateBranchRequestDto {

	private long branchId;
	private String displayName;
	private Organization branchType;
	private List<String> contacts;
	private List<String> emails;
	private List<Faq> branchFaqs;
	private Address branchAddress;
	private String branchImageUrl;
	private List<String> branchGalleryUrl;

}
