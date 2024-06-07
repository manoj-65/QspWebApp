package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long courseId;
	@Column(unique = true)
	private String courseName;
	@Column(columnDefinition = "text")
	private String courseDescription;

	@ElementCollection(targetClass = Organization.class)
	@CollectionTable(name = "branch_type_course", joinColumns = @JoinColumn(name = "course_id"))
	@Column(name = "branch_type")
	@Enumerated(EnumType.STRING)
	private List<Organization> branchType;

	private String courseIcon;

	@ElementCollection(targetClass = Mode.class)
	@CollectionTable(name = "mode_course", joinColumns = @JoinColumn(name = "course_id"))
	@Column(name = "mode")
	@Enumerated(EnumType.STRING)
	private List<Mode> mode;
	private String courseSummary;

	private String courseAbout;

	private String courseHighlight;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "course_subject", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
	private List<Subject> subjects = new ArrayList<Subject>();

	@ManyToMany(mappedBy = "courses", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@JsonIgnore
	private List<Category> categories = new ArrayList<>();

	@ManyToMany(mappedBy = "courses", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@JsonIgnore
	private List<SubCategory> subCategories = new ArrayList<SubCategory>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Batch> courseBatches = new ArrayList<Batch>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Review> courseReviews = new ArrayList<Review>();

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Faq> faqs = new ArrayList<Faq>();

	private String courseImage;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
