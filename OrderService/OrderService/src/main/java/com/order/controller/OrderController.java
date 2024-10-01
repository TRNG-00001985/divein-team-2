package com.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.dto.OrderRequest;
import com.order.model.Order;
import com.order.model.Status;
import com.order.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	//place order...
	@PostMapping("/place")
    @CircuitBreaker(name = "OrderService", fallbackMethod = "placeOrderFallback")
    @Retry(name = "InventoryService")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        try {
            String orderId = orderService.placeOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderId); 
        } catch (RuntimeException e) {
            return placeOrderFallback(request, e);
        }
    }
	

    public ResponseEntity<String> placeOrderFallback(OrderRequest request, RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Something went wrong, please try to order after sometime.");
    }
	
	
	//get Order details..
	@GetMapping("/{userId}")
	@CircuitBreaker(name="OrderService",fallbackMethod="getOrdersByUserIdFallback")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        
        return ResponseEntity.ok(orders);
    }
	
	public ResponseEntity<String> getOrdersByUserIdFallback(String userId,RuntimeException runtimeException){
		runtimeException.printStackTrace();
		return ResponseEntity.ok("Something went wrong, please try after sometime.");
	}
	
	//update Status....
	
	@PutMapping("/update")
	@CircuitBreaker(name="OrderService",fallbackMethod="updateStatusFallback")
	public ResponseEntity<Void> updateOrderStatus(@RequestParam("id") Long id, @RequestParam("status") Status status) {
        boolean updated = orderService.updateStatus(id, status);
        
        if (updated) {
            return ResponseEntity.ok().build(); 
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }
	public ResponseEntity<String> updateStatusFallback(@PathVariable Long id, @RequestBody Status status,RuntimeException runtimeException){
		runtimeException.printStackTrace();
		return ResponseEntity.ok("Something went wrong, please try after sometime.");
	}
	
	
	
	


}
