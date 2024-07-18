package com.alpha.qspiderrestapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alpha.qspiderrestapi.entity.enums.Role;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CustomAuthenticationManager {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTAuthenticationFiler authenticationFiler;

	private static final String[] PUBLIC_URLS = { "/swagger-apis/**", "/swagger-ui/**", "/v3/api-docs/**",
			"/swagger-ui.html", "/swagger-ui/index.html", "/api/{version}/users/login",
			"/api/{version}/users/getProfile", "/api/v1/categories/formresponse",
			"/api/{version}/categories/getAllCategories", "/api/{version}/categories/getbyid",
			"/api/{version}/subjects/course", "/api/{version}/branches/getAllBranches", "/api/{version}/feedback",
			"/api/{version}/courses/getbyid", "/api/{version}/branches/getbyid", "/api/{version}/courses/viewAll",
			"/api/{version}/enquiry", "/api/{version}/batches", "/api/{version}/categories/findAllCategories",
			"/api/{version}/faqs", "api/{version}/weightage/removeCategoryWeightage",
			"/api/{version}/weightage/removeSubCategoryWeightage", "/api/{version}/categories/onlineCourses" };

	private static final String[] ADMIN_URLS = { "/api/{version}/users/saveUser", "/api/{version}/categories",
			"/api/{version}/subcategories", "/api/{version}/categories/uploadIcon",
			"/api/{version}/subcategories/uploadIcon", "/api/{version}/batches" };

	private static final String[] COURSEADDER_URLS = { "/api/{version}/courses", "/api/{version}/subjects/getall",
			"/api/{version}/subjects", "/api/{version}/categories/getCategory", "/api/{version}/branches",
			"/api/{version}/courses/uploadImage", "/api/{version}/courses/uploadIcon",
			"/api/{version}/branches/uploadImages", "/api/{version}/branches/uploadIcon",
			"/api/{version}/branches/findAll", "/api/{version}/branches/modifyLocationUrl", "/api/{version}/faqs",
			"api/{version}/weightage/categories", "api/{version}/weightage/subCategories",
			"api/{version}/weightage/courses", "api/{version}/weightage/city", "/api/{version}/courses/saveCourse",
			"/api/{version}/weightage/updateCategoryWeightage", "api/{version}/weightage/categoryWeightage",
			"/api/{version}/courses/updateCourseContent", "/api/{version}/categories/removeCourseFromCategory",
			"/api/{version}/cities", "/api/{version}/subCategories/removeCourseFromSubCategory" };

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement(
						sessionManagment -> sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.requestMatchers(PUBLIC_URLS)
						.permitAll().requestMatchers(ADMIN_URLS).hasRole(Role.ADMIN.name())
						.requestMatchers(COURSEADDER_URLS).hasRole(Role.COURSEADDER.name()).anyRequest()
						.authenticated())
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authenticationFiler, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	public JwtAuthenticationEntryPoint authenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("GET", "POST", "PUT",
						"DELETE", "OPTIONS", "PATCH", "HEAD");
			}
		};
	}
}
