package com.example.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Seller;

public interface SellerRepo extends JpaRepository<Seller, String> {

}
