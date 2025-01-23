package com.example.Graduation_System.services;

import com.example.Graduation_System.data.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews();

    Review getReviewById(Long id);

    Review saveReview(Review review);

    void deleteReview(Long id);

}
