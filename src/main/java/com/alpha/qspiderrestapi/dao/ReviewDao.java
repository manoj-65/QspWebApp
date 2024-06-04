package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Review;

public interface ReviewDao {

	Review saveReview(Review review);

	Optional<Review> fetchReviewById(long reviewId);

	List<Review> fetchAllReviews();

	void deleteReview(long reviewId);

	long isReviewPresent(long reviewId);
}
