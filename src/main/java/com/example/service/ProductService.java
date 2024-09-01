package com.example.service;
import com.example.domain.Product;
import com.example.dto.ProductDTO;
import com.example.repository.AssetRepo;
import com.example.repository.ProductRepo;



import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepository;
    
    @Autowired
    private AssetRepo assetRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
//    public List<ProductDTO> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        List<ProductDTO> productDtos = new ArrayList<>();
//
//        for (Product product : products) {
//            int totalQuantity = product.getQuantity();
//            int usedQuantity = assetRepository.countByProductId(product.getProductId());
//            int availableQuantity = totalQuantity - usedQuantity;
//
//            ProductDTO productDto = new ProductDTO(product, availableQuantity);
//            productDtos.add(productDto);
//        }
//
//        return productDtos;
//    }
//    
    public List<Product> searchProducts(String keyword) {
        List<Product> products = productRepository.findAll();
        if (keyword != null && !keyword.trim().isEmpty()) {
            return products.stream()
                    .filter(p -> p.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                            p.getProductId().toString().equals(keyword))
                    .collect(Collectors.toList());
        }
        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    

    
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setSellerId(productDTO.getSellerId());
        product.setBrandType(productDTO.getBrandType());
        product.setCatalogueStatus(productDTO.getCatalogueStatus());
        product.setSellingAs(productDTO.getSellingAs());
        product.setCategoryName(productDTO.getCategoryName());
        product.setModel(productDTO.getModel());
        product.setHsnCode(productDTO.getHsnCode());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setWarranty(productDTO.getWarranty());
        product.setTaxes(productDTO.getTaxes());
        product.setTotal(productDTO.getTotal());
        product.setPurchaseDate(productDTO.getPurchaseDate());

        byte[] report1Bytes = productDTO.getReport1Bytes();
        byte[] report2Bytes = productDTO.getReport2Bytes();
        byte[] imageBytes = productDTO.getImageBytes();

        System.out.println("Report1 bytes length before saving: " + (report1Bytes != null ? report1Bytes.length : "null"));
        System.out.println("Report2 bytes length before saving: " + (report2Bytes != null ? report2Bytes.length : "null"));
        System.out.println("Image bytes length before saving: " + (imageBytes != null ? imageBytes.length : "null"));

        product.setReport1(report1Bytes);
        product.setReport2(report2Bytes);
        product.setImage(imageBytes);

        return productRepository.save(product);
    }
    
  
   

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public byte[] getReport1ById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            byte[] report1 = product.getReport1();
            if (report1 != null) {
                return report1;
            } else {
                System.out.println("Report1 is null for Product ID: " + id);
            }
        } else {
            System.out.println("Product not found with ID: " + id);
        }
        return null;
    }
    public byte[] getImageById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            byte[] image = product.getImage();
            if (image != null) {
                return image;
            } else {
                System.out.println("Image is null for Product ID: " + id);
            }
        } else {
            System.out.println("Product not found with ID: " + id);
        }
        return null;
    }

    @Transactional
    public Product updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        // Update fields that should always be updated
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setSellerId(productDTO.getSellerId());
        existingProduct.setBrandType(productDTO.getBrandType());
        existingProduct.setCatalogueStatus(productDTO.getCatalogueStatus());
        existingProduct.setSellingAs(productDTO.getSellingAs());
        existingProduct.setCategoryName(productDTO.getCategoryName());
        existingProduct.setModel(productDTO.getModel());
        existingProduct.setHsnCode(productDTO.getHsnCode());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setWarranty(productDTO.getWarranty());
        existingProduct.setTaxes(productDTO.getTaxes());
        existingProduct.setTotal(productDTO.getTotal());
        existingProduct.setPurchaseDate(productDTO.getPurchaseDate());

        // Check and update report1
        if (productDTO.getReport1Bytes() != null) {
            existingProduct.setReport1(productDTO.getReport1Bytes());
        }

        // Check and update report2
        if (productDTO.getReport2Bytes() != null) {
            existingProduct.setReport2(productDTO.getReport2Bytes());
        }

        // Check and update image
        if (productDTO.getImageBytes() != null) {
            existingProduct.setImage(productDTO.getImageBytes());
        }

        return productRepository.save(existingProduct);
    }

    private ProductDTO mapProductToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setSellerId(product.getSellerId());
        productDTO.setBrandType(product.getBrandType());
        productDTO.setCatalogueStatus(product.getCatalogueStatus());
        productDTO.setSellingAs(product.getSellingAs());
        productDTO.setCategoryName(product.getCategoryName());
        productDTO.setModel(product.getModel());
        productDTO.setHsnCode(product.getHsnCode());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setPrice(product.getPrice());
        productDTO.setWarranty(product.getWarranty());
        productDTO.setTaxes(product.getTaxes());
        productDTO.setTotal(product.getTotal());
        productDTO.setPurchaseDate(product.getPurchaseDate());
        productDTO.setReport1Bytes(product.getReport1());
        productDTO.setReport2Bytes(product.getReport2());
        productDTO.setImageBytes(product.getImage());
        return productDTO;
    }

    private void mapProductDTOToProduct(ProductDTO productDTO, Product product) {
        product.setProductName(productDTO.getProductName());
        product.setSellerId(productDTO.getSellerId());
        product.setBrandType(productDTO.getBrandType());
        product.setCatalogueStatus(productDTO.getCatalogueStatus());
        product.setSellingAs(productDTO.getSellingAs());
        product.setCategoryName(productDTO.getCategoryName());
        product.setModel(productDTO.getModel());
        product.setHsnCode(productDTO.getHsnCode());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setWarranty(productDTO.getWarranty());
        product.setTaxes(productDTO.getTaxes());
        product.setTotal(productDTO.getTotal());
        product.setPurchaseDate(productDTO.getPurchaseDate());
        product.setReport1(productDTO.getReport1Bytes());
        product.setReport2(productDTO.getReport2Bytes());
        product.setImage(productDTO.getImageBytes());
    }
    
    public ProductDTO getProductDTOById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return mapProductToProductDTO(product);
        }
        return null;
    }
    public long countExpiredWarranties() {
        List<Product> products = productRepository.findAll();

        LocalDate today = LocalDate.now();
        return products.stream()
                .filter(product -> {
                    LocalDate purchaseDate = product.getPurchaseDate().toLocalDate();
                    LocalDate warrantyExpiryDate = purchaseDate.plusYears(product.getWarranty());
                    return warrantyExpiryDate.isBefore(today);
                })
                .count();
    }
    
    
}
