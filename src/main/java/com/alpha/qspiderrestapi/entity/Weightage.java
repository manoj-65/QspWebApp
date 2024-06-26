package com.alpha.qspiderrestapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

}
