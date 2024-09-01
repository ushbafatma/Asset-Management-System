package com.example.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "asse")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asse_seq")
    @SequenceGenerator(name = "asse_seq", sequenceName = "asse_seq", allocationSize = 1)
    @Column(name = "ASSET_ID")
    private Long assetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Product_id", referencedColumnName = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) // Adjust fetch type as per your needs
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "LOCATION_ID")
    private Long locationId;
    
    @Column(name = "serial_number")
    private String serialNumber;

    @Column(nullable = true)
    private String status;

    // Constructors
    public Asset() {
        super();
    }

    // Getters and setters
    public Long getAssetId() {
        return assetId;
    }
    
    public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

    
