package com.alpha.qspiderrestapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.UserDto;
import com.alpha.qspiderrestapi.dto.UserProfile;
import com.alpha.qspiderrestapi.dto.UserRequest;
import com.alpha.qspiderrestapi.dto.UserResponse;
import com.alpha.qspiderrestapi.entity.User;

public interface UserService {

	ResponseEntity<ApiResponse<UserDto>> saveUser(User user);

	ResponseEntity<ApiResponse<UserResponse>> login(UserRequest userRequest);

	ResponseEntity<ApiResponse<UserProfile>> getUserProfile(String token);

	ResponseEntity<ApiResponse<List<UserDto>>> getAllCourseAdders();

	ResponseEntity<ApiResponse<List<UserDto>>> getAllTrainers();
}
