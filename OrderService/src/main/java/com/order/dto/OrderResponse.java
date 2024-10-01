package com.order.dto;

import java.util.List;

import org.hibernate.annotations.CurrentTimestamp;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
	private long orderId;
	private long buyerId;
	private Status status;
	private double totalAmount;
	private CurrentTimestamp orderDate;
	private String shippingAddress;
	private String billingAddress;
	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderDetails> orderItems;

}
