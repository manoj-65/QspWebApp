package com.alpha.qspiderrestapi.entity;

import com.alpha.qspiderrestapi.entity.validators.ValidEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FeedBack {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedBackId;
	@NotBlank(message = "Ensure that the User name is not null. Kindly enter a valid name.")
	private String userName;
	@NotBlank(message = "Ensure that the Phone Number not null. Kindly enter a valid Phone Number.")
	private String phoneNumber;
	@ValidEmail(message = "Ensure that the  Email is not null. Kindly enter a valid Email.")
	private String email;
	@Column(columnDefinition = "TEXT")
	@NotBlank(message = "Ensure that the Message is not null. Kindly enter a valid Feed Back.")
	private String message;
}
