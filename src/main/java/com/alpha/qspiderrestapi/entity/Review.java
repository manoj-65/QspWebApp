package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long reviewId;
	private double rating;
	private String comment;
	private String reviewedBy;

	@ManyToOne
	@JoinColumn(name = "branchId")
	private Branch branch;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "batchId")
	private Batch batch;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "courseId")
	private Course course;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
