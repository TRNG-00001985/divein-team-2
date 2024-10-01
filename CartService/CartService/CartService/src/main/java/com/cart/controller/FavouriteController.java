package com.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cart.dto.FavouriteRequest;
import com.cart.dto.FavouriteResponse;
import com.cart.service.FavouriteService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {
	
	private final FavouriteService favouriteService;

    @Autowired
    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }
    @PostMapping("/add")
    public ResponseEntity<FavouriteResponse> addFavourite(@RequestBody FavouriteRequest request) {
        FavouriteResponse response = favouriteService.addFavourite(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
//    // Add to favourites
//    @PostMapping("/add")
//    public String addFavourite(@RequestParam Long productId, @RequestParam String userId) {
//        favouriteService.addFavourite(productId, userId);
//        return "redirect:/favourites/view?userId=" + userId;  // Redirect back to view favourites
//    }
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<FavouriteResponse>> getFavouritesByUserId(@PathVariable String userId) {
//        List<FavouriteResponse> favourites = favouriteService.getFavouritesByUserId(userId);
//        return new ResponseEntity<>(favourites, HttpStatus.OK);
//    }
    
    
 // Get favourites by user ID and render JSP
    @GetMapping("/view")
    public ModelAndView getFavourites(@RequestParam String userId) {
        List<FavouriteResponse> favourites = favouriteService.getFavouritesByUserId(userId);
        ModelAndView mav = new ModelAndView("favourites");
        mav.addObject("favourites", favourites);
        mav.addObject("userId", userId);
        return mav;
    }
    
    
//    @DeleteMapping("/delete/{favouriteId}")
//    public ResponseEntity<Void> deleteFavourite(@PathVariable Long favouriteId) {
//        favouriteService.deleteFavouriteById(favouriteId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
 // Delete from-favourites..
    @PostMapping("/delete/{favouriteId}")
    public String deleteFavourite(@PathVariable Long favouriteId, @RequestParam String userId) {
        favouriteService.deleteFavouriteById(favouriteId);
        return "redirect:/favourites/view?userId=" + userId;  // Redirect back to view favourites
    }
    
 // Move to cart
    @PostMapping("/moveToCart/{favouriteId}")
    public String moveToCart(@PathVariable Long favouriteId, @RequestParam String userId) {
        favouriteService.moveToCart(favouriteId);
        return "redirect:/favourites/view?userId=" + userId;  // Redirect back to view favourites
    }
//    @PostMapping("/moveToCart/{favouriteId}")
//    public ResponseEntity<String> moveToCart(@PathVariable("favouriteId") Long favouriteId) {
//        boolean moved = favouriteService.moveToCart(favouriteId);
//        if (moved) {
//            return new ResponseEntity<>("item  moved to successfully",HttpStatus.OK);
//        }
//        return new ResponseEntity<>("failed to move item to cart",HttpStatus.NOT_FOUND);
//    }

}
