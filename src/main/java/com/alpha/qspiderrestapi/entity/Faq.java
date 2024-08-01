package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.alpha.qspiderrestapi.entity.enums.FaqType;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
//@JsonIgnoreProperties(value = {"createdDateAndTime","updatedDateAndTime"}, allowSetters = true)
public class Faq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long faqId;
	private String question;
	private String answer;

	@Enumerated(EnumType.STRING)
	private FaqType faqType;

	@Enumerated(EnumType.STRING)
	private Organization organizationType;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "branchId")
	private Branch branch;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "courseId")
	private Course course;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
