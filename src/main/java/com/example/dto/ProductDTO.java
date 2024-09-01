package com.example.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import com.example.domain.Product;

public class ProductDTO {
	
	private Long productId;
	private String productName;
    private String sellerId;
    private String brandType;
    private String catalogueStatus;
    private String sellingAs;
    private String categoryName;
    private String model;
    private String hsnCode;
    private int quantity;
    private BigDecimal price;
    private int warranty;
    private BigDecimal taxes;
    private BigDecimal total;
    private Date purchaseDate;

    // Assuming you want to handle file uploads for reports and image
    private MultipartFile report1;
    private MultipartFile report2;
    private MultipartFile image;
    
    private byte[] report1Bytes; // Add byte[] fields for reports and image
    private byte[] report2Bytes;
    private byte[] imageBytes;

    
    private Product product;
    private int availableQuantity;

    public ProductDTO(Product product, int availableQuantity) {
        this.product = product;
        this.availableQuantity = availableQuantity;
    }
    
	public ProductDTO() {
		super();
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getBrandType() {
		return brandType;
	}

	public void setBrandType(String brandType) {
		this.brandType = brandType;
	}

	public String getCatalogueStatus() {
		return catalogueStatus;
	}

	public void setCatalogueStatus(String catalogueStatus) {
		this.catalogueStatus = catalogueStatus;
	}

	public String getSellingAs() {
		return sellingAs;
	}

	public void setSellingAs(String sellingAs) {
		this.sellingAs = sellingAs;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getWarranty() {
		return warranty;
	}

	public void setWarranty(int warranty) {
		this.warranty = warranty;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public MultipartFile getReport1() {
		return report1;
	}

	public void setReport1(MultipartFile report1) {
		this.report1 = report1;
	}

	public MultipartFile getReport2() {
		return report2;
	}

	public void setReport2(MultipartFile report2) {
		this.report2 = report2;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public byte[] getReport1Bytes() {
		return convertMultipartFileToBytes(report1);
	}
	
	public void setReport1Bytes(byte[] report1Bytes) {
        this.report1Bytes = report1Bytes;
    }

	public byte[] getReport2Bytes() {
		return convertMultipartFileToBytes(report2);
	}
	
	 public void setReport2Bytes(byte[] report2Bytes) {
	        this.report2Bytes = report2Bytes;
	 }

	public byte[] getImageBytes() {
		return convertMultipartFileToBytes(image);
	}
	
	 public void setImageBytes(byte[] imageBytes) {
	        this.imageBytes = imageBytes;
	    }

	private byte[] convertMultipartFileToBytes(MultipartFile file) {
		try {
			return file != null ? file.getBytes() : null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
