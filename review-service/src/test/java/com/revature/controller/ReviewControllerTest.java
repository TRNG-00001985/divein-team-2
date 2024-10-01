package com.revature.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dto.ReviewRequest;
import com.revature.dto.ReviewResponse;
import com.revature.service.ReviewService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReviewRequest reviewRequest;
    private ReviewResponse reviewResponse;
    private List<ReviewResponse> reviewResponseList;

    @BeforeEach
    public void setUp() {
        reviewRequest = ReviewRequest.builder()
                .productId(1L)
                .userId("user_01")
                .rating(5)
                .comment("Great product!")
                .build();

        reviewResponse = ReviewResponse.builder()
                .reviewId(1L)
                .productId(1L)
                .userId("user_01")
                .rating(5)
                .comment("Great product!")
                .build();

        reviewResponseList = Arrays.asList(reviewResponse);
    }

    @Test
    public void addReview_ReturnsCreatedReview() throws Exception {
        BDDMockito.given(reviewService.addReview(any(ReviewRequest.class))).willReturn(reviewResponse);

        ResultActions response = mockMvc.perform(post("/api/review/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", CoreMatchers.is(reviewResponse.getProductId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(reviewResponse.getUserId().toString())));
    }

    @Test
    public void updateReview_ReturnsUpdatedReview() throws Exception {
        Long reviewId = 1L;

        BDDMockito.given(reviewService.updateReview(anyLong(), any(ReviewRequest.class))).willReturn(reviewResponse);

        ResultActions response = mockMvc.perform(put("/api/review/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest))
                .param("reviewId", String.valueOf(reviewId)));

        response.andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", CoreMatchers.is(reviewRequest.getProductId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyerId", CoreMatchers.is(reviewRequest.getUserId().toString())));
    }

    @Test
    public void deleteReview_ReturnsTrue() throws Exception {
        Long reviewId = 1L;

        BDDMockito.given(reviewService.deleteReview(anyLong())).willReturn(true);

        ResultActions response = mockMvc.perform(delete("/api/review/")
                .param("reviewId", String.valueOf(reviewId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.is(true)));
    }

    @Test
    public void getReviewsByProductId_ReturnsListOfReviews() throws Exception {
        Long productId = 1L;

        BDDMockito.given(reviewService.getReviewsByProductId(anyLong())).willReturn(reviewResponseList);

        ResultActions response = mockMvc.perform(get("/api/review/product")
                .param("productId", String.valueOf(productId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(reviewResponseList.size())));
    }

    @Test
    public void getAllReviewsByUserId_ReturnsListOfReviews() throws Exception {
        String userId = "user_01";

        BDDMockito.given(reviewService.getAllReviewsByUserId(toString())).willReturn(reviewResponseList);

        ResultActions response = mockMvc.perform(get("/api/review/user")
                .param("userId", String.valueOf(userId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(reviewResponseList.size())));
    }
}
