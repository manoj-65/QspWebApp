package com.alpha.qspiderrestapi.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private ObjectMapper objectMapper;

	@Override
	public final void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException e) throws IOException {
		final String expired = (String) request.getAttribute("expired");
		final String unsupported = (String) request.getAttribute("unsupported");
		final String invalid = (String) request.getAttribute("invalid");
		final String illegal = (String) request.getAttribute("illegal");
		final String notfound = (String) request.getAttribute("notfound");
		final String message;

		if (expired != null) {
			message = expired;
		} else if (unsupported != null) {
			message = unsupported;
		} else if (invalid != null) {
			message = invalid;
		} else if (illegal != null) {
			message = illegal;
		} else if (notfound != null) {
			message = notfound;
		} else {
			message = e.getMessage();
		}

		log.error("Could not set user authentication in security context. Error: {}", message);

//		ResponseEntity<ErrorResponse> responseEntity = new AppExceptionHandler(messageSourceService)
//				.handleBadCredentialsException(new BadCredentialsException(message));
		response.getWriter().write(objectMapper.writeValueAsString(message));
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	}
}
