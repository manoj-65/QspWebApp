package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weightage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long qspiders;
	private long jspiders;
	private long pyspiders;
	private long bspiders;

	private Long subCategory_categoryId;

	private Long course_categoryId;
	private Long course_SubCategoryId;

	@OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "categoryId")
	@JsonIgnore
	private Category category;

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "subCategoryId")
	@JsonIgnore
	private SubCategory subCategory;

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "courseId")
	@JsonIgnore
	private Course course;
	
	@OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "cityId")
	@JsonIgnore
	private City city;
	
	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
