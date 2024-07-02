package com.alpha.qspiderrestapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "view_all_home_page")
public class ViewAllHomePage {

	@Id
	private long branchId;
	private String cityName;
	private String cityImageUrl;
	private String branchType;
	private String displayName;
	private String branchImage;
	private String contacts;
	private long upcomingBatches;
	private long ongoingBatches;
	private String location;
	private String street;
	private String state;
	private String country;
	private int pincode;

}
