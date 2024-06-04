package com.alpha.qspiderrestapi.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.entity.User;
import com.alpha.qspiderrestapi.exception.InvaildCredentialsException;
import com.alpha.qspiderrestapi.repository.UserRepository;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userReposistory;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<User> optionalUser = userReposistory.findById(Long.parseLong(userId));
		if (optionalUser.isPresent()) {
			return createUserDetails(optionalUser.get());
		}
		throw new InvaildCredentialsException("Invalid email and password" + userId);
	}

	private UserDetails createUserDetails(User user) {
		return new ApplicationUserDetails(user);
	}

}