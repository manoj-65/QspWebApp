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

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/users")
public class UserController {
	@Autowired
	private UserService userService;

	@Hidden
	@Operation(description = "A User is saved into the database", summary = "Saves a user")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping("/saveUser")
	public ResponseEntity<ApiResponse<UserDto>> saveUser(@PathVariable String version, @RequestBody User user) {
		if (version.equalsIgnoreCase("V1"))
			return userService.saveUser(user);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}

	@Operation(description = "User is logged in with the right credentials", summary = "Logs in a user")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "OK", responseCode = "200"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserResponse>> login(@PathVariable String version,
			@RequestBody UserRequest userRequest) {
		if (version.equalsIgnoreCase("V1"))
			return userService.login(userRequest);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}
	
	@Operation(description = "Fetches the profile of a user based on a security token", summary = "Fetches a user profile")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "OK", responseCode = "200"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "400"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401") })
	@GetMapping("/getProfile")
	public ResponseEntity<ApiResponse<UserProfile>> getUserProfile(@PathVariable String version,
			@RequestHeader("Authorization") String token) {
		if (version.equalsIgnoreCase("v1"))
			return userService.getUserProfile(token);
		throw new UnauthorizedVersionException("Given Version is Build is Not Runing");
	}
	
	@Operation(description = "Fetches all the users with the role of Course Adder", summary = "Fetches CourseAdders")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Ok", responseCode = "200"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@GetMapping("/courseadders/getall")
	public ResponseEntity<ApiResponse<List<UserDto>>> getAllCourseadders(@PathVariable String version) {
		if (version.equalsIgnoreCase("V1"))
			return userService.getAllCourseAdders();
		throw new UnauthorizedVersionException("Unauthorized Version");
	}
	
	@Operation(description = "Fetches all the users with the role of Trainer", summary = "Fetches Trainers")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Ok", responseCode = "200"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@GetMapping("/trainers/getall")
	public ResponseEntity<ApiResponse<List<UserDto>>> getAllTrainers(@PathVariable String version) {
		if (version.equalsIgnoreCase("V1"))
			return userService.getAllTrainers();
		throw new UnauthorizedVersionException("Unauthorized Version");
	}
	
	@Hidden
	@Operation(description = "A Trainer (role based user) is saved into the database", summary = "Saves a trainer")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping("/trainer")
	public ResponseEntity<ApiResponse<UserDto>> saveTrainer(@PathVariable String version, @RequestBody UserDto user) {
		if (version.equalsIgnoreCase("V1"))
			return userService.saveTrainer(user);
		throw new UnauthorizedVersionException("Unauthorized Version");
	}
}
