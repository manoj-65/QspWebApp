package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long subjectId;
	@Column(unique = true)
	private String subjectTitle;
	private String subjectDescrption;

	@OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
	private List<Chapter> chapters = new ArrayList<Chapter>();

	@ManyToMany(mappedBy = "subjects")
	@JsonIgnore
	private List<Course> courses = new ArrayList<Course>();

//	private List<String> previews;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;
}
