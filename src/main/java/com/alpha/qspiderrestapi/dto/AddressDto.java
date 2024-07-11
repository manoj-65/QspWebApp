package com.alpha.qspiderrestapi.dto;

import lombok.Data;

//Request DTO not used 
@Data
public class AddressDto {
	private String state;
	private String city;
	private String street;
	private int pincode;
	private String location;
	private String country;
}
