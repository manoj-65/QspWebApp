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
	private String country;
	private String city;
	private int pinCode;
	private String state;
	private String street;
	private String organizationType;
	private long addressId;
	private long ctQspiders;
	private long ctJspiders;
	private long ctPyspiders;
	private long ctProspiders;
	private long qspiders;
	private long jspiders;
	private long pyspiders;
	private long prospiders;

}
