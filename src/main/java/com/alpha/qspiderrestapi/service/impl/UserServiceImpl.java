package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.UserDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.UserDto;
import com.alpha.qspiderrestapi.dto.UserProfile;
import com.alpha.qspiderrestapi.dto.UserRequest;
import com.alpha.qspiderrestapi.dto.UserResponse;
import com.alpha.qspiderrestapi.entity.User;
import com.alpha.qspiderrestapi.entity.enums.Role;
import com.alpha.qspiderrestapi.entity.enums.Status;
import com.alpha.qspiderrestapi.exception.UserNotFoundException;
import com.alpha.qspiderrestapi.modelmapper.UserDtoMapper;
import com.alpha.qspiderrestapi.security.ApplicationUserDetailsService;
import com.alpha.qspiderrestapi.security.JwtTokenProvider;
import com.alpha.qspiderrestapi.service.UserService;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserUtil userUtil;

	@Autowired
	private ApplicationUserDetailsService applicationUserDetailsService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public ResponseEntity<ApiResponse<UserDto>> saveUser(User user) {
		user.setEmails(userUtil.mapEmailToUser(user.getEmails(), user));
		user.setContacts(userUtil.mapPhoneNumberToUser(user.getContacts(), user));
		user = userDao.saveUser(user);
		return ResponseUtil.getCreated(UserDtoMapper.mapUserToUserDto(user));
	}

	@Override
	public ResponseEntity<ApiResponse<UserResponse>> login(UserRequest userRequest) {
		log.info("Entering login with userRequest: {}", userRequest);

		if (userRequest.getEmail() != null && !userRequest.getEmail().isBlank()
				&& (userRequest.getContact() == null || userRequest.getContact().isBlank())) {
			log.info("Trying to login with email: {}", userRequest.getEmail());
			Optional<User> optionalUser = userDao.findUserByUserEmailAndUserPassword(userRequest.getEmail(),
					userRequest.getPassword());

			if (optionalUser.isPresent()) {
				log.info("User found with email: {}", userRequest.getEmail());
				return validate(optionalUser.get());
			} else {
				log.error("User with the given email and password not found: {}", userRequest.getEmail());
				throw new UserNotFoundException("User with the Given Email and Password Not Found");
			}
		} else if (userRequest.getContact() != null && !userRequest.getContact().isBlank()
				&& userRequest.getContact().length() == 10
				&& (userRequest.getEmail() == null || userRequest.getEmail().isBlank())) {
			log.info("Trying to login with contact: {}", userRequest.getContact());
			long phoneNumber = 0;
			try {
				phoneNumber = Long.parseLong(userRequest.getContact());
			} catch (Exception e) {
				throw new UserNotFoundException("Enter A Valid PhoneNumber");
			}
			Optional<User> optionalUser = userDao.findUserByUserPhoneNumberAndUserPassword(phoneNumber,
					userRequest.getPassword());

			if (optionalUser.isPresent()) {
				log.info("User found with contact: {}", userRequest.getContact());
				return validate(optionalUser.get());
			} else {
				log.error("User with the given contact and password not found: {}", userRequest.getContact());
				throw new UserNotFoundException("User with the Given Contact and Password Not Found");
			}
		}
		throw new UserNotFoundException("Invalid Login Credentials");
	}

	private ResponseEntity<ApiResponse<UserResponse>> validate(User user) {
		log.info("Entering validate with user: {}", user);
		if (user != null && user.getStatus().equals(Status.ACTIVE)) {
			log.info("User of  id: {} is active", user.getUserId());
			UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(user.getUserId() + "");
			String jwtToken = jwtTokenProvider.generateToken(userDetails, user.getUserId() + "", user.getRole().name());
			user.setUserToken(jwtToken);
			user = userDao.saveUser(user);
			log.info("User saved with new token: {}", user.getUserToken());
			return ResponseUtil.getOk(UserResponse.builder().role(user.getRole()).token(user.getUserToken()).build());
		}
		log.error("User is not active or null: {}", user != null ? user.getUserId() : "null");
		throw new UserNotFoundException("User Is Not Active Contact the Admin");

	}

	@Override
	public ResponseEntity<ApiResponse<UserProfile>> getUserProfile(String token) {
		log.info("Entering getUserProfile with token: {}", token);

		if (token != null && !token.isBlank()) {
			token = token.substring(7);
			log.info("Extracted token: {}", token);

			String userId = (String) jwtTokenProvider.extractAllClaims(token).get("userId");
			log.info("Extracted userId from token: {}", userId);

			if (userId != null && !userId.isBlank()) {
				Optional<User> optionalUser = userDao.findById(Long.parseLong(userId));

				if (optionalUser.isPresent()) {
					User user = optionalUser.get();
					log.info("User found with userId: {}", userId);

					if (user.getRole().equals(Role.COURSEADDER) || user.getRole().equals(Role.ADMIN)) {
						UserProfile userProfile = new UserProfile();
						userProfile.setUserId(user.getUserId());
						userProfile.setUserName(user.getUserName());
						log.info("Returning user profile for userId: {}", userId);
						return ResponseUtil.getOk(userProfile);
					} else {
						log.error("User role is not authorized for userId: {}", userId);
						throw new UserNotFoundException("User Not Authorized");
					}
				} else {
					log.error("User not found with userId: {}", userId);
					throw new UserNotFoundException("User Not Found");
				}
			} else {
				log.error("Invalid token, userId is null or blank");
				throw new UserNotFoundException("Invalid Token");
			}
		} else {
			log.error("Token is null or blank");
			throw new UserNotFoundException("Token Not Exists");
		}
	}

	@Override
	public ResponseEntity<ApiResponse<List<UserDto>>> getAllCourseAdders() {
		List<User> allCourseAdders = userDao.getUsersByRole(Role.COURSEADDER);
		List<UserDto> userDtos = new ArrayList<UserDto>();
		allCourseAdders.forEach(user -> userDtos.add(UserDtoMapper.mapUserToUserDto(user)));
		return ResponseUtil.getOk(userDtos);
	}

	@Override
	public ResponseEntity<ApiResponse<List<UserDto>>> getAllTrainers() {
		List<User> allCourseAdders = userDao.getUsersByRole(Role.TRAINER);
		List<UserDto> userDtos = new ArrayList<UserDto>();
		allCourseAdders.forEach(user -> userDtos.add(UserDtoMapper.mapUserToUserDto(user)));
		return ResponseUtil.getOk(userDtos);
	}

}
