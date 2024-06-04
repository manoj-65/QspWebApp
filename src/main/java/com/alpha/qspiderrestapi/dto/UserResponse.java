package com.alpha.qspiderrestapi.dto;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
	private Role role;
	private String token;
}
