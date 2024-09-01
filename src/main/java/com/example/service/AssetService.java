package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AssetRepo;
import com.example.repository.LocationRepo;
import com.example.repository.ProductRepo;
import com.example.domain.Asset;
import com.example.domain.Location;
import com.example.domain.Product;

@Service
public class AssetService {
	
	@Autowired
    private AssetRepo repo;
	@Autowired
    private LocationRepo locrepo;
	@Autowired
    private ProductRepo prodrepo;
	
	public List<Location> getProductLocations(Long productId) {
        return locrepo.findLocationsByProductId(productId);
    }
	public List<Asset> listAll() {
        return repo.findAll();
    }
	public void save(Asset asset) {
		Product product = asset.getProduct();
        product.decrementQuantity();
        prodrepo.save(product);
        repo.save(asset);
    }
	public void saveAll(List<Asset> assets) {
        repo.saveAll(assets);
    }
	public List<Asset> findByEmployeeId(Long employeeId) {
        return repo.findByEmployee_EmployeeId(employeeId);
    }
	public long countAssets() {
        return repo.count();
    }
//	public long getAssetCountByProductAndLocation(Long productId, Long locationId) {
//        return repo.countByProductIdAndLocationId(productId, locationId);
//    }
	 public Asset getAssetById(Long id) {
	        return repo.findById(id).orElse(null);
	    }
	 public List<Asset> getAssetsByLocationId(Long locationId) {
	        return repo.findByLocationId(locationId);
	    }
	 
	 public void updateStatus(Long assetId, String status) {
	        Asset asset = repo.findById(assetId)
	                                     .orElseThrow(() -> new RuntimeException("Asset not found"));
	        asset.setStatus(status);
	        repo.save(asset);
	    }
	 public List<Asset> getAssetsRequiringService() {
	        return repo.findByStatus("Servicing Required");
	    }
	 public long getServicingRequiredCount() {
	        return repo.countByStatus("Servicing Required");
	    }
	 
	 public boolean updateAssetStatusToAssigned(Long assetId) {
	        Optional<Asset> assetOptional = repo.findById(assetId);
	        if (assetOptional.isPresent()) {
	            Asset asset = assetOptional.get();
	            asset.setStatus("Assigned");
	            repo.save(asset);
	            return true;
	        }
	        return false;
	    }
//
	    public void deleteAsset(Long id) {
	        repo.deleteById(id);
	    }

}
