package com.alpha.qspiderrestapi.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alpha.qspiderrestapi.exception.InvaildCredentialsException;
import com.alpha.qspiderrestapi.exception.UserNotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFiler extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private ApplicationUserDetailsService applicationUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// verifying the token starting from the second request.

		String authorization = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;

		if (authorization != null && authorization.startsWith("Bearer ")) {
			jwtToken = authorization.substring(7);

			try {
				username = tokenProvider.extractUsername(jwtToken);
			} catch (Exception e) {
				throw new InvaildCredentialsException("Invalied Credentials");
			}
			UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} else {
				throw new UserNotFoundException("Invalid User Detalies");
			}
		}
		filterChain.doFilter(request, response);
	}
}
