package com.example.aspect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.domain.Asset;
import com.example.domain.Employee;
import com.example.domain.Location;
import com.example.domain.Product;
import com.example.domain.Seller;
import com.example.repository.AssetRepo;
import com.example.repository.EmployeeRepo;
import com.example.repository.LocationRepo;
import com.example.repository.ProductRepo;
import com.example.repository.SellerRepo;
import com.example.service.AuditService;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private AuditService auditService;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private SellerRepo sellerRepository;

    @Autowired
    private AssetRepo assetRepository;

    @Autowired
    private LocationRepo locationRepository;

    @Pointcut("execution(* com.example.service.*.save*(..)) || " +
              "execution(* com.example.service.*.update*(..)) || " +
              "execution(* com.example.service.*.delete*(..))")
    public void serviceMethods() {}

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        String action;
        String details;
        if (methodName.startsWith("save")) {
            action = determineActionForSaveMethod(className, args);
            details = getDetailsForSaveOrUpdate(className, args, action);
        } else if (methodName.startsWith("update")) {
            action = "UPDATE";
            details = getDetailsForSaveOrUpdate(className, args, action);
        } else if (methodName.startsWith("delete")) {
            action = "DELETE";
            details = Arrays.toString(args);
        } else {
            action = "UNKNOWN";
            details = Arrays.toString(args);
        }

        String tableName = getTableName(className);

        auditService.logAction(tableName, action, details);
    }

    private String determineActionForSaveMethod(String className, Object[] args) {
        if (args != null && args.length > 0) {
            switch (className) {
                case "ProductService":
                    if (args[0] instanceof Product) {
                        Product product = (Product) args[0];
                        if (product.getProductId() != null && productRepository.existsById(product.getProductId())) {
                            return "UPDATE";
                        }
                    }
                    break;
                case "EmployeeService":
                    if (args[0] instanceof Employee) {
                        Employee employee = (Employee) args[0];
                        if (employee.getEmployeeId() != null && employeeRepository.existsById(employee.getEmployeeId())) {
                            return "UPDATE";
                        }
                    }
                    break;
                case "SellerService":
                    if (args[0] instanceof Seller) {
                        Seller seller = (Seller) args[0];
                        if (seller.getSellerId() != null && sellerRepository.existsById(seller.getSellerId())) {
                            return "UPDATE";
                        }
                    }
                    break;
                case "AssetService":
                    if (args[0] instanceof Asset) {
                        Asset asset = (Asset) args[0];
                        if (asset.getAssetId() != null && assetRepository.existsById(asset.getAssetId())) {
                            return "UPDATE";
                        }
                    }
                    break;
                case "LocationService":
                    if (args[0] instanceof Location) {
                        Location location = (Location) args[0];
                        if (location.getLocationId() != null && locationRepository.existsById(location.getLocationId())) {
                            return "UPDATE";
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return "INSERT";
    }

    private String getDetailsForSaveOrUpdate(String className, Object[] args, String action) {
        if (args != null && args.length > 0) {
            switch (className) {
                case "ProductService":
                    if (args[0] instanceof Product) {
                        Product product = (Product) args[0];
                        if ("UPDATE".equals(action)) {
                            Product oldProduct = productRepository.findById(product.getProductId()).orElse(null);
                            return formatDetails(product, oldProduct);
                        }
                    }
                    break;
                case "EmployeeService":
                    if (args[0] instanceof Employee) {
                        Employee employee = (Employee) args[0];
                        if ("UPDATE".equals(action)) {
                            Employee oldEmployee = employeeRepository.findByEmployeeId(employee.getEmployeeId());
                            return formatDetails(employee, oldEmployee);
                        }
                    }
                    break;
                case "SellerService":
                    if (args[0] instanceof Seller) {
                        Seller seller = (Seller) args[0];
                        if ("UPDATE".equals(action)) {
                            Seller oldSeller = sellerRepository.findById(seller.getSellerId()).orElse(null);
                            return formatDetails(seller, oldSeller);
                        }
                    }
                    break;
                case "AssetService":
                    if (args[0] instanceof Asset) {
                        Asset asset = (Asset) args[0];
                        if ("UPDATE".equals(action)) {
                            Asset oldAsset = assetRepository.findById(asset.getAssetId()).orElse(null);
                            return formatDetails(asset, oldAsset);
                        }
                    }
                    break;
                case "LocationService":
                    if (args[0] instanceof Location) {
                        Location location = (Location) args[0];
                        if ("UPDATE".equals(action)) {
                            Location oldLocation = locationRepository.findById(location.getLocationId()).orElse(null);
                            return formatDetails(location, oldLocation);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return Arrays.toString(args);
    }

    private String formatDetails(Object newObj, Object oldObj) {
        if (newObj == null || oldObj == null) {
            return "New: " + newObj + ", Old: " + oldObj;
        }
        StringBuilder details = new StringBuilder();
        Field[] fields = newObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(newObj);
                Object oldValue = field.get(oldObj);
                if (!Objects.equals(newValue, oldValue)) {
                    details.append(field.getName()).append(": ")
                           .append("Old Value: ").append(oldValue).append(", ")
                           .append("New Value: ").append(newValue).append("; ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return details.toString().isEmpty() ? "No changes detected" : details.toString();
    }

    private String getTableName(String className) {
        switch (className) {
            case "LocationService":
                return "location";
            case "AssetService":
                return "asset";
            case "EmployeeService":
                return "employee";
            case "ProductService":
                return "product";
            case "SellerService":
                return "seller";
            default:
                return "unknown";
        }
    }
}
