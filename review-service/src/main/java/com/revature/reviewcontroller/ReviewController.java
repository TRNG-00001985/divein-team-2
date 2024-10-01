package com.revature.reviewcontroller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revature.dto.ReviewRequest;
import com.revature.dto.ReviewResponse;
import com.revature.service.ReviewService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api/review/")
public class ReviewController {
	
	private static final Logger logger= LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    
    @CircuitBreaker(name = "reviewService", fallbackMethod = "fallbackAddReview")
    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(@RequestBody ReviewRequest reviewRequest) {
    	
    	logger.info("Received request to add a review for product:{}",reviewRequest.getProductId());
    	
    	try {
    		ReviewResponse response = reviewService.addReview(reviewRequest);
    		logger.info("Successfully added review for product:{}",reviewRequest.getProductId());
    		
        return new ResponseEntity<>(reviewService.addReview(reviewRequest), HttpStatus.CREATED);
    	}
    	catch(Exception e) {
    		logger.error("failed to add review for product:{}",reviewRequest.getProductId(), e);
    		return new ResponseEntity<>(reviewService.addReview(reviewRequest),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    public ResponseEntity<ReviewResponse> fallbackAddReview(ReviewRequest reviewRequest, Throwable t) {
        logger.error("Fallback - Failed to add review for product: {}", reviewRequest.getProductId(), t);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    
    @CircuitBreaker(name = "reviewService", fallbackMethod = "fallbackDeleteReview")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteReview(@RequestParam Long reviewId) {
    	 logger.info("Received request to delete review with ID: {}", reviewId);
         try {
             boolean deleted = reviewService.deleteReview(reviewId);
             if (deleted) {
                 logger.info("Successfully deleted review with ID: {}", reviewId);
        return new ResponseEntity<>(reviewService.deleteReview(reviewId), HttpStatus.OK);
             } 
             else 
             {
                logger.warn("Failed to delete review with ID: {}", reviewId);
                 return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
             }
         } 
         catch (Exception e) {
            logger.error("Error while deleting review with ID: {}", reviewId, e);
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    
    public ResponseEntity<Boolean> fallbackDeleteReview(Long reviewId, Throwable t) {
        logger.error("Fallback - Failed to delete review with ID: {}", reviewId, t);
        return new ResponseEntity<>(false, HttpStatus.SERVICE_UNAVAILABLE);
    }
    

    @CircuitBreaker(name = "reviewService", fallbackMethod = "fallbackUpdateReview")
    @PutMapping
    public ResponseEntity<ReviewResponse> updateReview(@RequestParam Long reviewId, @RequestBody ReviewRequest reviewRequest) {
    	
    	 logger.info("Received request to update review with ID: {}", reviewId);
         try {
             ReviewResponse response = reviewService.updateReview(reviewId, reviewRequest);
             
             logger.info("Successfully updated review with ID: {}", reviewId);
        return new ResponseEntity<>(reviewService.updateReview(reviewId, reviewRequest), HttpStatus.ACCEPTED);
         }
         catch (Exception e) {
        	 logger.error("Failed to update review with ID:{}",reviewId, e);
        	 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    
    public ResponseEntity<ReviewResponse> fallbackUpdateReview(Long reviewId, ReviewRequest reviewRequest, Throwable t) {
        logger.error("Fallback - Failed to update review with ID: {}", reviewId, t);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    
    @CircuitBreaker(name = "reviewService", fallbackMethod = "fallbackGetReviewsByProductId")
    @GetMapping("product")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@RequestParam Long productId) {
    	
    	logger.info("Fetching reviews for productId: {}", productId);
        
    	try {
            
    		List<ReviewResponse> reviews = reviewService.getReviewsByProductId(productId);
           logger.info("Successfully fetched reviews for productId: {}", productId);
        return new ResponseEntity<>(reviewService.getReviewsByProductId(productId), HttpStatus.OK);
    	}
    	catch(Exception e) {
    		logger.error("Error while fetching reviews for productId:{}",productId, e);
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    public ResponseEntity<List<ReviewResponse>> fallbackGetReviewsByProductId(Long productId, Throwable t) {
        logger.error("Fallback - Failed to fetch reviews for productId: {}", productId, t);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    
    
    @CircuitBreaker(name = "reviewService", fallbackMethod = "fallbackGetAllReviewsByUserId")
    @GetMapping("user")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsByUserId(@RequestParam String userId) {
    	 logger.info("Fetching reviews for userId: {}", userId);
        
    	 try {
            
    		 List<ReviewResponse> reviews = reviewService.getAllReviewsByUserId(userId);
            logger.info("Successfully fetched reviews for userId: {}", userId);
             return new ResponseEntity<>(reviewService.getAllReviewsByUserId(userId), HttpStatus.OK);
    	 }
    	 catch(Exception e) {
    		 logger.error("Error while fetching reviews for userId:{}", userId, e);
    		 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    		}
    }
    public ResponseEntity<List<ReviewResponse>> fallbackGetAllReviewsByUserId(String userId, Throwable t) {
        logger.error("Fallback - Failed to fetch reviews for userId: {}", userId, t);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}

