package com.alpha.qspiderrestapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedBack {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedBackId;
	private String userName;
	@Min(value = 5000000000l, message = "Ensure that the PhoneNumber startes with 5 on words and having 10 Digit. Kindly enter a valid PhoneNumber.")
	@Max(value = 9999999999l, message = "Ensure that the  PhoneNumber is only 10 Digit. Kindly enter a valid PhoneNumber.")
	private Long mobileNumber;
	@Email(message = "Ensure that the  Email is not null. Kindly enter a valid Email.")
	private String email;
	@Column(columnDefinition = "TEXT")
	private String message;
}
