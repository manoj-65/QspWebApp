package com.alpha.qspiderrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.UserDto;
import com.alpha.qspiderrestapi.dto.UserProfile;
import com.alpha.qspiderrestapi.dto.UserRequest;
import com.alpha.qspiderrestapi.dto.UserResponse;
import com.alpha.qspiderrestapi.entity.User;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/saveUser")
	public ResponseEntity<ApiResponse<UserDto>> saveUser(@PathVariable String version, @RequestBody User user) {
		if (version.equalsIgnoreCase("V1"))
			return userService.saveUser(user);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserResponse>> login(@PathVariable String version,
			@RequestBody UserRequest userRequest) {
		if (version.equalsIgnoreCase("V1"))
			return userService.login(userRequest);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}
	
	@GetMapping("/getProfile")
	public ResponseEntity<ApiResponse<UserProfile>> getUserProfile(@PathVariable String version,
			@RequestHeader("Authorization") String token) {
		if (version.equalsIgnoreCase("v1"))
			return userService.getUserProfile(token);
		throw new UnauthorizedVersionException("Given Version is Build is Not Runing");
	}
	
	@GetMapping("/courseadders/getall")
	public ResponseEntity<ApiResponse<List<UserDto>>> getAllCourseadders(@PathVariable String version) {
		if (version.equalsIgnoreCase("V1"))
			return userService.getAllCourseAdders();
		throw new UnauthorizedVersionException("Unauthorized Version");
	}
}
