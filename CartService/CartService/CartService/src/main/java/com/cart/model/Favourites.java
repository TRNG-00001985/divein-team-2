package com.cart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="favourites")
public class Favourites {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long favouriteId;
	private long productId;
	private String userId;

}
//
//
////move to cart
////add to favourites
////delete from favourities by favourite_id
////get favourities by user_id

//for carts
//move to favourites


