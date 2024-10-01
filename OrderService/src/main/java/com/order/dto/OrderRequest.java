package com.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.order.model.OrderDetails;
import com.order.model.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
	private String userId;
	private Status status;
	private double totalAmount;
	private LocalDateTime orderDate;
	private String shippingAddress;
	private String billingAddress;
	private List<OrderDetailsDto> orderDetailsList;

}
