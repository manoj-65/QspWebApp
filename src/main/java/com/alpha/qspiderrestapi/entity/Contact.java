package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"createdDateAndTime","updatedDateAndTime"}, allowSetters = true)
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contactId;
	@NotBlank(message = "Ensure that the Country Code is not null. Kindly enter a valid CountryCode.")
	private String countryCode;

	@Column(unique = true)
	@Min(value = 999999999l, message = "Ensure that the PhoneNumber having 10 Digit. Kindly enter a valid PhoneNumber.")
	@Max(value = 9999999999l, message = "Ensure that the  PhoneNumber is only 10 Digit. Kindly enter a valid PhoneNumber.")
	private Long phoneNumber;

	@ManyToOne
	@JoinColumn
	@JsonIgnore
	private User user;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
