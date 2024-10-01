package com.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cart.model.Favourites;

public interface FavouriteRepository extends JpaRepository<Favourites,Long> {
	List<Favourites>	findByUserId(String UserId);

}
