package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domain.Location;

public interface LocationRepo extends JpaRepository<Location, Long> {
	@Query("SELECT l FROM Location l JOIN Asset a ON l.locationId = a.locationId WHERE a.product.productId = :productId")
    List<Location> findLocationsByProductId(@Param("productId") Long productId);

}
