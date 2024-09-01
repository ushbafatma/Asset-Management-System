package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
	
//	 @Query("SELECT COUNT(p) FROM Product p WHERE p.purchaseDate <= CURRENT_DATE - p.warranty")
//	    long countExpiredWarranties();
	 
}

