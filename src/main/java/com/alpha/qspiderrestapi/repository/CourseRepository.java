package com.alpha.qspiderrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.qspiderrestapi.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	/**
	 * Finds a course by its ID.
	 *
	 * This method fetches a course record from the database based on the provided
	 * course ID.
	 *
	 * @param courseId The ID of the course to retrieve.
	 * @return An Integer representing the course ID if found, otherwise null.
	 */
	@Query(value = "Select c.courseId From Course c Where c.courseId = :courseId")
	Long findByCourseId(@Param("courseId") long courseId);

	/**
	 * Assigns a subject to a course.
	 *
	 * This method inserts a new record into the `course_subject` table, associating
	 * the provided course ID with the provided subject ID.
	 *
	 * @param courseId  The ID of the course to associate the subject with.
	 * @param subjectId The ID of the subject to be assigned to the course.
	 * @throws RuntimeException If an error occurs during the database operation.
	 */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO course_subject(course_id, subject_id) VALUES (:courseId,:subjectId)", nativeQuery = true)
	void assignSubjectToCourse(long courseId, long subjectId);

	@Query(value = "Select course_id From course_subject Where subject_id = :subjectId AND course_id = :courseId", nativeQuery = true)
	Long findBySubjectId(@Param("subjectId") long subjectId, @Param("courseId") long courseId);

	@Transactional
	@Modifying
	@Query(value = "delete from sub_category_course where  course_id = :courseId", nativeQuery = true)
	int removeCourseAndSubCategoryById(@Param("courseId") long courseId);

	@Transactional
	@Modifying
	@Query(value = "delete from category_course where  course_id = :courseId", nativeQuery = true)
	int removeCourseAndCategoryById(@Param("courseId") long courseId);
	
//    @Query("SELECT c FROM Course c JOIN c.batches bt WHERE bt.batchStatus = 'UPCOMING' AND bt.branch.branchId = :branchId")
//    List<Course> findCoursesWithUpcomingBatches(@Param("branchId") Long branchId);

}
