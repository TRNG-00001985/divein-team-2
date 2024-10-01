package com.revature.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.dto.ReviewRequest;
import com.revature.dto.ReviewResponse;
import com.revature.model.Review;
import com.revature.repository.ReviewRepository;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;
    private ReviewRequest reviewRequest;
    private ReviewResponse reviewResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        review = new Review();
        review.setReviewId(11L);
        review.setProductId(7L);
        review.setUserId("user_01");
        review.setRating(5);
        review.setComment("Great product!");

        reviewRequest = new ReviewRequest();
        reviewRequest.setProductId(7L); // This should match the product ID in `review`
        reviewRequest.setUserId("user_01"); // This should match the buyer ID in `review`
        reviewRequest.setRating(5);
        reviewRequest.setComment("Great product!");
    }

    @Test
    public void addReview_ReturnsReviewResponse() {
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        ReviewResponse reviewResponse = reviewService.addReview(reviewRequest);

        assertThat(reviewResponse).isNotNull();
        assertThat(reviewResponse.getProductId()).isEqualTo(review.getProductId());
        assertThat(reviewResponse.getUserId()).isEqualTo(review.getUserId());
        assertThat(reviewResponse.getRating()).isEqualTo(review.getRating());
        assertThat(reviewResponse.getComment()).isEqualTo(review.getComment());
    }

    @Test
    public void updateReview_ReturnsUpdatedReviewResponse() {
        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        ReviewResponse updatedReview = reviewService.updateReview(11L, reviewRequest);

        assertThat(updatedReview).isNotNull();
        assertThat(updatedReview.getRating()).isEqualTo(5);
        assertThat(updatedReview.getComment()).isEqualTo("Great product!");
    }

    @Test
    public void updateReview_ThrowsExceptionWhenReviewNotFound() {
        given(reviewRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reviewService.updateReview(11L, reviewRequest);
        });
    }

    @Test
    public void deleteReview_ReturnsTrue() {
        given(reviewRepository.existsById(anyLong())).willReturn(true); // Ensure review exists
        boolean isDeleted = reviewService.deleteReview(11L);

        assertThat(isDeleted).isTrue();
    }

    @Test
    public void deleteReview_ThrowsExceptionWhenDeleteFails() {
        doThrow(new RuntimeException("Delete failed")).when(reviewRepository).deleteById(anyLong());

        boolean isDeleted = reviewService.deleteReview(11L);

        assertThat(isDeleted).isFalse();
    }

    @Test
    public void getReviewsByProductId_ReturnsListOfReviews() {
        given(reviewRepository.findByProductId(anyLong())).willReturn(List.of(review));

        List<ReviewResponse> reviews = reviewService.getReviewsByProductId(7L);

        assertThat(reviews).isNotEmpty();
        assertThat(reviews.size()).isEqualTo(1);
    }

    @Test
    public void getAllReviewsByUserId_ReturnsListOfReviews() {
        given(reviewRepository.findByUserId(toString())).willReturn(List.of(review));

        List<ReviewResponse> reviews = reviewService.getAllReviewsByUserId("user_01");

        assertThat(reviews).isNotEmpty();
        assertThat(reviews.size()).isEqualTo(1);
    }
}
