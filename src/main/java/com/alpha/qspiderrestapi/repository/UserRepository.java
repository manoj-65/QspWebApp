package com.alpha.qspiderrestapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alpha.qspiderrestapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT u FROM User u WHERE u.userId IN(SELECT e.user.userId FROM Email e WHERE e.email=?1) AND u.userPassword=?2")
	Optional<User> findUserByUserEmailAndUserPassword(String userEmail, String userPassword);

	@Query(value = "SELECT u FROM User u WHERE u.userId IN(SELECT c.user.userId FROM Contact c WHERE c.phoneNumber=?1) AND u.userPassword=?2")
	Optional<User> findUserByUserPhoneNumberAndUserPassword(long phoneNumber, String password);

	@Query(value = "SELECT u FROM User u WHERE u.userId IN(SELECT e.user.userId FROM Email e WHERE e.email=?1)")
	Optional<User> findUserByEmail(String email);

	@Query(value = "SELECT u FROM User u WHERE u.userId IN(SELECT c.user.userId FROM Contact c WHERE c.phoneNumber=?1)")
	Optional<User> findUserByUserPhoneNumber(long phoneNumber);

	@Query(value = "SELECT u FROM User u WHERE u.role='COURSEADDER'")
	List<User> findUserByRole();
}
