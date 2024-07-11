package com.alpha.qspiderrestapi.dto;

import java.util.List;

import com.alpha.qspiderrestapi.entity.Address;
import com.alpha.qspiderrestapi.entity.Faq;
import com.alpha.qspiderrestapi.entity.enums.Organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchRequestDto {

	private String displayName;
	private List<String> contacts;
	private Organization branchType;
	private Address branchAddress;
	private List<Faq> branchFaqs;
}
