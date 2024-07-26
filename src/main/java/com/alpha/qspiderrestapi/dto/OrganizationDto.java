package com.alpha.qspiderrestapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrganizationDto {

	private String organization;
	private List<BranchDto> branches;
}
