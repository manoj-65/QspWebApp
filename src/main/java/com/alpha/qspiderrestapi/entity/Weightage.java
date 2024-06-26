package com.alpha.qspiderrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weightage {

	@Id
	private long id;
	private long qspiders;
	private long jspiders;
	private long pyspiders;
	private long bspiders;

	private long subCategory_categoryId;

	private long course_categoryId;
	private long course_SubCategoryId;

	@OneToOne(mappedBy = "weightage")
	@JsonIgnore
	private Category category;

	@ManyToOne
	@JoinColumn(name = "subCategoryId")
	private SubCategory subCategory;

	@ManyToOne
	@JoinColumn(name = "courseId")
	private Course course;

}
