package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.ReviewDao;
import com.alpha.qspiderrestapi.entity.Review;
import com.alpha.qspiderrestapi.repository.ReviewRepository;

@Repository
public class ReviewDaoImpl implements ReviewDao {

	@Autowired
	private ReviewRepository reviewRepository;

	@Override
	public Review saveReview(Review review) {
		return reviewRepository.save(review);
	}

	@Override
	public Optional<Review> fetchReviewById(long reviewId) {
		return reviewRepository.findById(reviewId);
	}

	@Override
	public List<Review> fetchAllReviews() {
		return reviewRepository.findAll();
	}

	@Override
	public void deleteReview(long reviewId) {
		reviewRepository.deleteById(reviewId);
	}

	@Override
	public long isReviewPresent(long reviewId) {
		return reviewRepository.findByReviewId(reviewId);
	}

}
