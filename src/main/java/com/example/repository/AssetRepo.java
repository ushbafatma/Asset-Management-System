package com.example.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.Asset;

@Repository
public interface AssetRepo extends JpaRepository<Asset, Long> {
	
	List<Asset> findByEmployee_EmployeeId(Long employeeId);
	
long countByStatus(String status);
	
	
	List<Asset> findByStatus(String status);
	
	 List<Asset> findByLocationId(Long locationId);
	
	@Query("SELECT COUNT(a) FROM Asset a WHERE a.product.productId = :productId")
    int countByProductId(@Param("productId") Long productId);
	
//	@Query("SELECT COUNT(a) FROM Asset a WHERE a.product.productId = :productId AND a.location.locationId = :locationId")
//    long countByProductIdAndLocationId(@Param("productId") Long productId, @Param("locationId") Long locationId);

	
//	 @Query("SELECT a, e FROM Asset a JOIN Employee e ON a.employeeId = e.employeeId WHERE a.locationId = :locationId")
//	 List<Object[]> findAssetsWithEmployeesByLocation(Long locationId);
}
