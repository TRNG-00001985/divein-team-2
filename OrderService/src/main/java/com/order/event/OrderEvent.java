package com.order.event;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.order.model.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor 
public class OrderEvent {

	
	private long orderId;
	private String userId;
	private Status status;
	private double totalAmount;
	@CreationTimestamp
	private LocalDateTime orderDate; 
	private String shippingAddress;
	private String billingAddress;
	
}
