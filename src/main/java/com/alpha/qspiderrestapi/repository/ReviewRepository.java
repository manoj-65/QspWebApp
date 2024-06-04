package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query(value = "Select r.reviewId From Review r Where r.reviewId = :reviewId")
	Long findByReviewId(@Param("reviewId") long reviewId);
}
