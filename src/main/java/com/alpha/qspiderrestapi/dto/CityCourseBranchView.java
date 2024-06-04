package com.alpha.qspiderrestapi.dto;

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
@Table(name = "citycoursebranchview")
public class CityCourseBranchView {

	@Id
	private long branchId;
	private String courseName;
	private String branchTitle;
	private String city;
	private String country;

}
