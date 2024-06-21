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
	private String city;
	private String cityIconUrl;
	private long courseId;
	private String courseName;
	private String displayName;
	private String branchImage;
	private String location;
	private String contacts;
	private long upcomingBatches; 
	private long ongoingBatches;
	private String cityImageUrl;
	private long branchCount;
	private String courseIcon;
	private String courseDescription;

}
