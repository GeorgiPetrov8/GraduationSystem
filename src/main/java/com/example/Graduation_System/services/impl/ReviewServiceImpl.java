package com.example.Graduation_System.services.impl;

import com.example.Graduation_System.data.Review;
import com.example.Graduation_System.data.repo.ReviewRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with ID " + id + " not found."));
    }

    @Override
    public Review saveReview(Review review) {
        if (review == null) {
            throw new ValidationException("Review cannot be null.");
        }
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Review with ID " + id + " does not exist.");
        }
        reviewRepository.deleteById(id);
    }

}
