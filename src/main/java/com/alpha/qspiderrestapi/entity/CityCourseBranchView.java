package com.alpha.qspiderrestapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city_course_branch_view")
public class CityCourseBranchView {

	@Id
	private long id;
	private long branchId;
	private String country;
	private String street;
	private String state;
	private int pincode;
	private long addressId;
	private String city;
	private String cityIconUrl;
	private long courseId;
	private String courseName;
	private String displayName;
	private String branchImage;
	private String branch_type;
	private String location;
	private String contacts;
	private long upcomingBatches;
	private long ongoingBatches;
	private String cityImageUrl;
	private long branchCount;
	private String courseIcon;
	private String courseDescription;
	private long qspiders;
	private long jspiders;
	private long pyspiders;
	private long prospiders;
	private long cQspiders;
	private long cJspiders;
	private long cPyspiders;
	private long cProspiders;
	private Long ctQspiders;
	private Long ctJspiders;
	private Long ctPyspiders;
	private Long ctProspiders;

}
