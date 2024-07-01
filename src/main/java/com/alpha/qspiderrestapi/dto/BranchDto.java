package com.alpha.qspiderrestapi.dto;

import lombok.Data;

@Data
public class BranchDto {

	private long branchId;
	private String branchName;
	private String phoneNumber;
	private String location;
	private String branchImage;
	private Long upcomingBatches;
	private Long ongoingBatches;
	private String city;
	private int pinCode;
	private String state;
	private String street;
	private String organizationType;
	private long addressId;
}
