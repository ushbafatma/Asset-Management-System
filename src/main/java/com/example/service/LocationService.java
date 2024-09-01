package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Location;
import com.example.repository.LocationRepo;
import jakarta.transaction.Transactional;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepo repo;
	
	public long countLocations() {
        return repo.count();
    }
	public void saveAll(List<Location> locations) {
        repo.saveAll(locations);
    }

	public List<Location> listAll() {
	 return repo.findAll();
	}

	public Location getLocationById(Long locationId) {
	 return repo.findById(locationId).orElseThrow();
	}

	@Transactional
	public Location save(Location location) {
	 return repo.save(location);
	}

	public Location updateSeller(Location location) {
	 return repo.save(location);
	}

	public void deleteLocation(Long locationId) {
	 repo.deleteById(locationId);
	}

}
