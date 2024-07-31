package com.alpha.qspiderrestapi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.alpha.qspiderrestapi.entity.enums.BatchStatus;
import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long batchId;
	private String batchTitle;
	private String trainerName;

	private LocalDate startingDate;
	private LocalDate endingDate;
	private LocalTime startingTime;
	private LocalTime endingTime;

	private int extendingDays;

	@OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
	private List<Review> batchReviews = new ArrayList<Review>();

	@ManyToOne
	@JoinColumn(name = "courseId")
	@JsonIgnore
	private Course course;
	
	@Enumerated(EnumType.STRING)
	private Mode batchMode;

	@ManyToOne
	@JoinColumn(name = "branchId")
	@JsonIgnore
	private Branch branch;
	
	@Enumerated(EnumType.STRING)
	private BatchStatus batchStatus;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;
}
