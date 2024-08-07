package com.alpha.qspiderrestapi.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.Contact;
import com.alpha.qspiderrestapi.entity.Email;
import com.alpha.qspiderrestapi.entity.enums.Role;
import com.alpha.qspiderrestapi.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(value = {"userPassword"}, allowSetters = true)
public class UserDto {
	private String userName;
	private List<Email> userEmail;
	private List<Contact> phoneNumber;
	private Role role;
	private Status status;
	private String userPassword;
}
