package com.example.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "loc")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loc_seq")
    @SequenceGenerator(name = "loc_seq", sequenceName = "loc_SEQ", allocationSize = 1)
    @Column(name = "LOCATION_ID")
    private Long locationId;
    @Column(name = "LOCATION_NAME")
    private String location_name;
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
    
    
}