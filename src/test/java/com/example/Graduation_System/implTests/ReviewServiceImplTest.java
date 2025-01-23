package com.example.Graduation_System.implTests;

import com.example.Graduation_System.data.Review;
import com.example.Graduation_System.data.repo.ReviewRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        review = new Review();
        review.setId(1L);
        review.setText("Excellent thesis.");
    }

    @Test
    void testGetAllReviews() {

        List<Review> reviews = Arrays.asList(review);
        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> result = reviewService.getAllReviews();

        assertNotNull(result);
        assertEquals(reviews.size(), result.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testGetReviewById() {

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Review result = reviewService.getReviewById(1L);

        assertNotNull(result);
        assertEquals(review.getId(), result.getId());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testGetReviewByIdNotFound() {

        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewById(1L));
        assertEquals("Review with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testSaveReview() {

        when(reviewRepository.save(review)).thenReturn(review);

        Review result = reviewService.saveReview(review);

        assertNotNull(result);
        assertEquals(review.getId(), result.getId());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testSaveReviewWithNullReview() {

        ValidationException exception = assertThrows(ValidationException.class, () -> reviewService.saveReview(null));
        assertEquals("Review cannot be null.", exception.getMessage());
    }

    @Test
    void testDeleteReview() {

        when(reviewRepository.existsById(1L)).thenReturn(true);

        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteReviewNotFound() {

        when(reviewRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(1L));
        assertEquals("Cannot delete. Review with ID 1 does not exist.", exception.getMessage());
    }
}
