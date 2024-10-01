package com.revature.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.dto.ReviewRequest;
import com.revature.dto.ReviewResponse;
import com.revature.model.Review;
import com.revature.repository.ReviewRepository;

@Service
public class ReviewService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
       
    }

   
    private Review mapToReview(ReviewRequest reviewRequest) {
    	
    	logger.debug("Mapping ReviewRequest to review entity for productId:{}",reviewRequest.getProductId());
       
//        if (!productRepository.existsById(reviewRequest.getProductId())) {
//            throw new RuntimeException("Product not found");
//        }

        return Review.builder()
                .productId(reviewRequest.getProductId())
                .buyerId(reviewRequest.getBuyerId())
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .build();
    }

   
    private ReviewResponse mapToReviewResponse(Review review) {
    	
    	logger.debug("Mapping Review entity to ReviewResponse for reviewId:{}",review.getReviewId());
    	 return new ReviewResponse(
                 review.getReviewId(),
                 review.getProductId(),
                 review.getBuyerId(),
                 review.getRating(),
                 review.getComment(),
                 review.getReviewDate());
    }

    
    public ReviewResponse addReview(ReviewRequest reviewRequest) {
    	
    	logger.info("Adding a new review for productd:{}",reviewRequest.getProductId());
        Review review = mapToReview(reviewRequest);
        review = reviewRepository.save(review);
        return mapToReviewResponse(review);
    }

  
    public ReviewResponse updateReview(Long reviewId,ReviewRequest reviewRequest) {
    	
    	logger.info("Updating review with reviewId:{}",reviewId);
    	
        Optional<Review> existingReview = reviewRepository.findById(reviewId);

        if (existingReview.isPresent()) {
            Review review = mapToReview(reviewRequest);
            review.setReviewId(reviewId);
            review = reviewRepository.save(review);
           logger.info("Successfully updated review with reviewID:{}",reviewId);
            return mapToReviewResponse(review);
        } else {
        	
        	logger.warn("Review not found with Id:{}",reviewId);
            throw new RuntimeException("Review not found with ID: " + reviewId);
        }
    }

   
    public boolean deleteReview(Long reviewId) {
    	  logger.info("Deleting review with reviewId: {}", reviewId);
    	
        try {
            reviewRepository.deleteById(reviewId);
            logger.info("Successfully deleted review with reviewId: {}", reviewId);
            return true;
        } catch (Exception e) {
        	logger.error("Error occurred while deleting review with reviewId: {}", reviewId, e);
            return false;
        }
    }

    
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
    	
    	logger.info("Fetching reviews for productId: {}", productId);
        List<Review> reviews = reviewRepository.findByProductId(productId); 
        return reviews.stream().map(this::mapToReviewResponse).toList();
    }

   
    public List<ReviewResponse> getAllReviewsByBuyerId(Long buyerId) {
    	logger.info("Fetching reviews for productId:{}",buyerId);
    	List<Review> reviews = reviewRepository.findByBuyerId(buyerId);
        return reviews.stream().map(this::mapToReviewResponse).toList();
    }
}
