 package com.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.dto.OrderDetailsDto;
import com.order.dto.OrderRequest;
import com.order.event.OrderEvent;
import com.order.model.Order;
import com.order.model.OrderDetails;
import com.order.model.Status;
import com.order.repository.OrderDetailsRepository;
import com.order.repository.OrderRepository;

import reactor.core.publisher.Mono;

@Service
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final WebClient webClient;
	private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
	@Autowired
	public OrderService(OrderRepository orderRepository,WebClient webClient,OrderDetailsRepository orderDetailsRepository, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
		this.orderRepository=orderRepository;
		this.webClient=webClient;
		this.kafkaTemplate= kafkaTemplate;
		
	}
	
	public List<Boolean> isInStock(List<OrderDetailsDto> list){
		try {
			Mono<List<Boolean>> itemStatus=webClient.post()
					.uri("instock")
					.bodyValue(list)
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<Boolean>>() {});
			return itemStatus.block();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String placeOrder(OrderRequest request)  {
		
		List<Boolean> itemsInStock=isInStock(request.getOrderDetailsList());
		
		for(Boolean inStock:itemsInStock) {
			if(inStock==false) {
				return "Product is not in stock";
			}
		}
		
		Order order=new Order();
		order.setUserId(request.getUserId());
		order.setStatus(request.getStatus());
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setTotalAmount(request.getTotalAmount());
		order.setShippingAddress(request.getShippingAddress());
		order.setBillingAddress(request.getBillingAddress());
		order.setOrderDate(LocalDateTime.now());
		
		List<OrderDetails> orderDetails=request.getOrderDetailsList()
				.stream()
				.map(this::mapTOOrderDetails)
				.toList();
		order.setOrderDetailsList(orderDetails);	
				
		orderRepository.save(order);
		kafkaTemplate.send("notificationTopic",new OrderEvent(order.getOrderId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getOrderDate(),
                order.getShippingAddress(),
                order.getBillingAddress()));
		return "Order Placed Successfull";
	}
	
	
	public OrderDetails mapTOOrderDetails(OrderDetailsDto orderDetailsdto) {
		return OrderDetails.builder()
				.price(orderDetailsdto.getPrice())
				.skuCode(orderDetailsdto.getSkuCode())
				.quantity(orderDetailsdto.getQuantity())
				.build();
	}
	
	
	public boolean updateStatus(Long id,Status status) {
		Optional<Order> optionalOrder=orderRepository.findById(id);
		if(optionalOrder!=null) {
			Order order=orderRepository.findById(id).get();
			order.setStatus(status);
			orderRepository.save(order);
			return true;
		}
		return false;
		
	}
	
	public List<Order> getOrdersByUserId(String userId) {
		List<Order> order=orderRepository.findByUserId(userId);
		
		return order;
		
	}
	
	
}
