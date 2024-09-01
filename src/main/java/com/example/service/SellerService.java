package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.SellerRepo;

import jakarta.transaction.Transactional;

import com.example.domain.Seller;

//SellerService.java
@Service
public class SellerService {

@Autowired
private SellerRepo sellerRepository;

public List<Seller> listAll() {
 return sellerRepository.findAll();
}

public void saveAll(List<Seller> sellers) {
    sellerRepository.saveAll(sellers);
}

public Seller getSellerById(String SellerId) {
 return sellerRepository.findById(SellerId).orElseThrow();
}

@Transactional
public Seller save(Seller seller) {
 return sellerRepository.save(seller);
}

public Seller updateSeller(Seller seller) {
 return sellerRepository.save(seller);
}

public void deleteSeller(String SellerId) {
 sellerRepository.deleteById(SellerId);
}
}
