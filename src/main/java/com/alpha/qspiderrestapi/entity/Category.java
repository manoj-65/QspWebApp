package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long categoryId;
	@Column(unique = true)
	private String categoryTitle;

	private String categoryIcon;
	private String categoryAlternativeIcon;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<SubCategory> subCategories = new ArrayList<SubCategory>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "category_course", joinColumns = @JoinColumn(name = "categoryId"), inverseJoinColumns = @JoinColumn(name = "courseId"))
	private List<Course> courses = new ArrayList<Course>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private Weightage weightage;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
