package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.dto.UserDto;
import com.alpha.qspiderrestapi.entity.User;

public interface UserDao {
	User saveUser(User user);

	Optional<User> findUserByUserEmailAndUserPassword(String userEmail, String userPassword);

	Optional<User> findUserByUserPhoneNumberAndUserPassword(long phoneNumber, String password);

	Optional<User> findById(long userId);

	Optional<User> findUserByEmail(String email);

	Optional<User> findUserByUserPhoneNumber(long phoneNumber);

	List<User> getAllCourseAdders();

}
