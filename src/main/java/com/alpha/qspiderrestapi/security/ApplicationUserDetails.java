package com.alpha.qspiderrestapi.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alpha.qspiderrestapi.entity.User;

@SuppressWarnings("serial")
public class ApplicationUserDetails implements UserDetails {

	private User user;

	public ApplicationUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

	}

	@Override
	public String getPassword() {
		return user.getUserPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserId() + "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
