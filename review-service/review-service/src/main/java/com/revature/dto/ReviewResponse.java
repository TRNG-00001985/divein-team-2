package com.revature.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
	
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class ReviewResponse {
	
	    private Long reviewId;
	    private Long productId;
	    private Long buyerId;
	    private int rating;
	    private String comment;
	    private LocalDateTime reviewDate;

}
