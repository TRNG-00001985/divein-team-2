package com.cart.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewController {
	
	@GetMapping("/cart")
	public String viewCart(Model model) {
		return "cart";
	}
	@GetMapping("/address")
	public String address(Model model) {
		return "address";
	}
	@GetMapping("/favourite")
	public String favourites(Model model) {
		return "favourites";
	}
}
