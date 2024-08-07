package com.alpha.qspiderrestapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.exception.UnauthorizedVersionException;
import com.alpha.qspiderrestapi.service.SubjectService;
import com.alpha.qspiderrestapi.service.impl.SubjectServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for handling Subject-related endpoints.
 * 
 * @see SubjectServiceImpl
 * @see ApiResponse
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/{version}/subjects")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	/**
	 * Endpoint to save a Subject entity.
	 *
	 * @param version the API version.
	 * @param subject the Subject entity to be saved.
	 * @return a ResponseEntity containing the ApiResponse with the saved Subject.
	 * @throws UnauthorizedVersionException if the API version is not authorized.
	 */
	@Operation(description = "A Independent Subject is saved into the database", summary = "Saves a Subject")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Success", responseCode = "201"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "401"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(content = @Content(), responseCode = "404") })
	@PostMapping
	public ResponseEntity<ApiResponse<Subject>> saveSubject(@PathVariable String version,
			@RequestBody Subject subject) {
		if (version.equalsIgnoreCase("V1"))
			return subjectService.saveSubject(subject);
		throw new UnauthorizedVersionException("Unauthorized Version");

	}

	@GetMapping("/course")
	public ResponseEntity<ApiResponse<List<Subject>>> fetchAllSubjectsOfCourse(@PathVariable String version,
			@RequestParam long courseId) {
		if (version.equalsIgnoreCase("V1"))
			return subjectService.fetchAllSubjectsOfCourse(courseId);
		throw new UnauthorizedVersionException("Unauthorized Version");

	}

	@GetMapping
	public ResponseEntity<ApiResponse<Subject>> fetchSubjectById(@PathVariable String version,
			@RequestParam long subjectId) {
		if (version.equalsIgnoreCase("V1"))
			return subjectService.fetchSubjectById(subjectId);
		throw new UnauthorizedVersionException("Unauthorized Version");

	}

	@GetMapping("/getall")
	public ResponseEntity<ApiResponse<List<Subject>>> fetchAllSubjects(@PathVariable String version) {
		if (version.equalsIgnoreCase("V1"))
			return subjectService.fetchAllSubjects();
		throw new UnauthorizedVersionException("Unauthorized Version");

	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable String version, @RequestParam long subjectId) {
		if (version.equalsIgnoreCase("V1"))
			return subjectService.deleteById(subjectId);
		throw new UnauthorizedVersionException("Unauthorized Version");

	}

//	@PatchMapping("/editSubjects")
//	public ResponseEntity<ApiResponse<Subject>> updateSubject(@PathVariable String version,
//			@RequestBody Subject subject, @RequestBody long subjectId) {
//		if (version.equalsIgnoreCase("V1"))
//			return subjectService.updateSubject(subject, subjectId);
//		throw new UnauthorizedVersionException("Unauthorized Version");
//
//	}

}
