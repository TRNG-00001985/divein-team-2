package com.revature.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name="reviews")

public class Review {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long reviewId;
	
	 @Column(nullable = false)
	    private Long productId;
	 
	@Column(nullable = false)
    private String userId;
	

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

//    @Column(name = "review_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
//    private CurrentTimestamp reviewDate;
    
    @CreationTimestamp
    @Column(name = "review_date", updatable = false)
    private LocalDateTime reviewDate;

}
