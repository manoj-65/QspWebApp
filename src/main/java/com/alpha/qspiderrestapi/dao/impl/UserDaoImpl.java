package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.UserDao;
import com.alpha.qspiderrestapi.dto.UserDto;
import com.alpha.qspiderrestapi.entity.User;
import com.alpha.qspiderrestapi.repository.UserRepository;

@Repository
public class UserDaoImpl implements UserDao {
	@Autowired
	private UserRepository userRepository;

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findUserByUserEmailAndUserPassword(String userEmail, String userPassword) {
		return userRepository.findUserByUserEmailAndUserPassword(userEmail, userPassword);
	}

	@Override
	public Optional<User> findUserByUserPhoneNumberAndUserPassword(long phoneNumber, String password) {
		return userRepository.findUserByUserPhoneNumberAndUserPassword(phoneNumber, password);
	}

	@Override
	public Optional<User> findById(long userId) {
		return userRepository.findById(userId);
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Override
	public Optional<User> findUserByUserPhoneNumber(long phoneNumber) {
		return userRepository.findUserByUserPhoneNumber(phoneNumber);
	}

	@Override
	public List<User> getAllCourseAdders() {
		return userRepository.findUserByRole();
	}

}
