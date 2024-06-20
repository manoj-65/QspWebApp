package com.alpha.qspiderrestapi.entity;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city_branch_count_view")
public class CityBranchView {

	@Id
	private long branchId;
	private String city;
	private String cityImageUrl;
	private Long branchCount;
}
