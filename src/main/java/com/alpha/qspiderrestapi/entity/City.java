package com.alpha.qspiderrestapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "city_icon_image")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cityIconId;
	@Column(unique = true)
	private String cityName;
	private String cityIconUrl;
	private String cityImageUrl;
	@Column(nullable = true)
	private long branchCount;
}
