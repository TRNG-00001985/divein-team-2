package com.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productservice.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
	
}
