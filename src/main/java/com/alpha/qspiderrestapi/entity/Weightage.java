package com.alpha.qspiderrestapi.entity;

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

	private long subCategory_categoryId;

	private long course_categoryId;
	private long course_SubCategoryId;

	@OneToOne(mappedBy = "weightage",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JsonIgnore
	private Category category;

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "subCategoryId")
	private SubCategory subCategory;

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "courseId")
	private Course course;

}
