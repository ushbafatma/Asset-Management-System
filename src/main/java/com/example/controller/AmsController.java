package com.example.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.opencsv.CSVReader;

import java.util.ArrayList;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.example.service.LocationService;
import com.example.domain.Location;
import com.example.domain.Log;
import com.example.service.AssetService;
import com.example.domain.Asset;
import com.example.domain.Employee;
import com.example.domain.Product;
import com.example.domain.Seller;
import com.example.dto.ProductDTO;
import com.example.repository.LogRepository;
import com.example.service.EmployeeService;
import com.example.service.ProductService;
import com.example.service.SellerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AmsController {
	
    @Autowired
    private EmployeeService empService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ProductService prodService;
    @Autowired
    private LocationService locService;
    @Autowired
    private AssetService assetService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("employeeId") Long employeeId, @RequestParam("password") String password, Model model, HttpServletRequest request) {
        // Log the received credentials
        System.out.println("Attempting login for employee ID: " + employeeId);
        System.out.println("Attempting login for password: " + password);

        Employee oauthUser = empService.login(employeeId, password);

        if (oauthUser != null) {
            // Log successful login
            System.out.println("Login successful for employee ID: " + oauthUser.getEmployeeId());

            // Store user in session
            HttpSession session = request.getSession();
            session.setAttribute("user", oauthUser);

            // Redirect based on user role
            if ("A".equals(oauthUser.getRole())) {
                return "redirect:/admin-dashboard";
            } else {
                return "redirect:/employee-dashboard";
            }
        } else {
            // Log failed login
            System.out.println("Invalid username or password for employee ID: " + employeeId);

            // If login fails, show error message
            model.addAttribute("errorMessage", "Invalid username or password.");
            return "login";
        }
    }
    
    @GetMapping("/adduser")
    public String add(Model model) {
        model.addAttribute("employee", new Employee());
        return "adduser";
    }
    
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("employee") Employee std, HttpServletRequest request) {
        empService.save(std);
        return "admin-dashboard";
    }
    
    @GetMapping("/employee")
    public String listEmployees(Model model) {
        List<Employee> employees = empService.listAll();
        model.addAttribute("employees", employees);
        return "employee";
    }
//    @PostMapping("/upload")
//    public String uploadCSV(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//        if (file.isEmpty()) {
//            redirectAttributes.addFlashAttribute("message", "Please select a CSV file to upload.");
//            return "redirect:/employees";
//        }
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            List<Employee> employees = new ArrayList<>();
//            String line;
//            boolean firstLine = true;
//
//            while ((line = reader.readLine()) != null) {
//                if (firstLine) {
//                    firstLine = false; // Skip header line
//                    continue;
//                }
//                String[] fields = line.split(",");
//                Employee employee = new Employee();
//                employee.setEmployeeId(Long.parseLong(fields[0]));
//                employee.setName(fields[1]);
//                employee.setDate_of_joining(Date.valueOf(fields[2]));
//                employee.setPassword(fields[3]);
//                employee.setPhone_no(fields[4]);
//                employee.setRole(fields[5]);
//                employees.add(employee);
//            }
//            empService.saveAll(employees);
//            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("message", "An error occurred while processing the CSV file: " + e.getMessage());
//        }
//
//        return "redirect:/employees";
//    }

//    @PostMapping("/employees/upload")
//    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//        if (file.isEmpty()) {
//            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
//            return "redirect:uploadStatus";
//        }
//
//        try {
//            empService.saveEmployeesFromFile(file);
//            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("message", "Failed to upload file: " + e.getMessage());
//        }
//
//        return "redirect:/uploadStatus";
//    }
//   
    @GetMapping("/employees/{employeeId}/edit")
    public ModelAndView showEditEmployeePage(@PathVariable("employeeId") Long employeeId) {
        ModelAndView mav = new ModelAndView("new"); 
        Employee employee = empService.getByEmployeeId(employeeId); // Call the service method to get employee by email
        mav.addObject("employee", employee);
        return mav;
    }
    
    @Autowired
    private LogRepository logRepository;
    
    @GetMapping("/log")
    public String listLogs(Model model) {
        List<Log> logs = logRepository.findAll();
        model.addAttribute("logs", logs);
        return "logs";
    }
    
    @GetMapping("/employees/{id}/assets")
    public String viewEmployeeAssets(@PathVariable("id") long emp_id, Model model) {
        List<Asset> assets = assetService.findByEmployeeId(emp_id);
        model.addAttribute("assets", assets);
        model.addAttribute("employeeId", emp_id);  // Add employeeId to the model
        return "employee_assets";
    }
    
    @GetMapping("/employees/{employeeId}/delete")
    public String deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        // Delete employee from the database
        empService.deleteByEmployeeId(employeeId);
        return "redirect:/employee";
    }

    @PostMapping("/savechanges")
    public String saveEmployeeChanges(@ModelAttribute("employee") Employee employee) {
    	Employee existingEmployee = empService.getByEmployeeId(employee.getEmployeeId());
        
        // Updating fields that are allowed to change
        existingEmployee.setEmployeeId(employee.getEmployeeId());
        existingEmployee.setName(employee.getName());
        existingEmployee.setDate_of_joining(employee.getDate_of_joining());
        existingEmployee.setPhone_no(employee.getPhone_no());
        existingEmployee.setRole(employee.getRole());
        
        // Save the updated employee data
        empService.save(existingEmployee);
        return "redirect:/employee"; 
    }
    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
    	model.addAttribute("employeeCount", empService.countEmployees());
    	model.addAttribute("locationCount", locService.countLocations());
    	model.addAttribute("assetCount",assetService.countAssets());
    	model.addAttribute("serviceRequiredCount", assetService.getServicingRequiredCount());
    	model.addAttribute("warrantyExpiredCount", prodService.countExpiredWarranties());
        return "admin-dashboard";
    }
    
    @PostMapping("/product/bulk-upload")
    public String bulkUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            return "redirect:/listproduct";
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            
            // Skip the header row
            if (rows.size() > 0) {
                rows.remove(0);
            }

            for (String[] row : rows) {
                Product product = new Product();
                product.setProductName(row[0]);
                product.setSellerId(row[1]);
                product.setBrandType(row[2]);
                product.setCatalogueStatus(row[3]);
                product.setSellingAs(row[4]);
                product.setCategoryName(row[5]);
                product.setModel(row[6]);
                product.setHsnCode(row[7]);
                product.setQuantity(Integer.parseInt(row[8]));
                product.setPrice(new BigDecimal(row[9]));
                product.setWarranty(Integer.parseInt(row[10]));
                product.setTaxes(new BigDecimal(row[11]));
                product.setTotal(new BigDecimal(row[12]));
                product.setPurchaseDate(Date.valueOf(row[13]));
                // Assuming report1, report2, and image are not included in the CSV

                prodService.saveProduct(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "An error occurred while processing the CSV file.");
        }

        return "redirect:/listproduct";
    }
    @PostMapping("/assets/upload")
    public String bulkUploadAssets(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Asset> assets = new ArrayList<>();
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] fields = line.split(",");
                Asset asset = new Asset();
                asset.setProduct(prodService.getProductById(Long.parseLong(fields[0])));
                asset.setEmployee(empService.getByEmployeeId(Long.parseLong(fields[1])));
                asset.setLocationId(Long.parseLong(fields[2]));
                asset.setStatus(fields[3]);
                assets.add(asset);
            }
            assetService.saveAll(assets);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/listassets";
    }
    
    @PostMapping("/sellers/upload")
    public String bulkUploadSellers(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Seller> sellers = new ArrayList<>();
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] fields = line.split(",");
                Seller seller = new Seller();
                seller.setSellerId(fields[0]);
                seller.setCompanyName(fields[1]);
                seller.setContactNo(fields[2]);
                seller.setEmailId(fields[3]);
                seller.setAddress(fields[4]);
                seller.setMsmeVerified(Boolean.parseBoolean(fields[5]));
                seller.setMSMERegistrationNo(fields[6]);
                seller.setGSTIN(fields[7]);
                sellers.add(seller);
            }
            sellerService.saveAll(sellers);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/listallseller";
    }
    
    @PostMapping("/locations/upload")
    public String bulkUploadLocations(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Location> locations = new ArrayList<>();
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                Location location = new Location();
                location.setLocation_name(line.trim());
                locations.add(location);
            }
            locService.saveAll(locations);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/listlocation";
    }
    
    @PostMapping("/employees/upload")
    public String bulkUploadEmployees(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Employee> employees = new ArrayList<>();
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] fields = line.split(",");
                Employee employee = new Employee();
                employee.setEmployeeId(Long.parseLong(fields[0].trim()));
                employee.setName(fields[1].trim());
                employee.setDate_of_joining(Date.valueOf(fields[2].trim()));
                employee.setPassword(fields[3].trim());
                employee.setPhone_no(fields[4].trim());
                employee.setRole(fields[5].trim());
                employees.add(employee);
            }
            empService.saveAll(employees);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/employee";
    }
    
    @GetMapping("/assets-requiring-service")
    public String getAssetsRequiringServicing(Model model) {
        List<Asset> assetsRequiringService = assetService.getAssetsRequiringService();
        model.addAttribute("assets", assetsRequiringService);
        return "assets-requiring-service"; // This should match the HTML template name without the extension
    }
    @PostMapping("/asset/{id}/complete-service")
    public String completeService(@PathVariable("id") Long assetId, RedirectAttributes redirectAttributes) {
        boolean updated = assetService.updateAssetStatusToAssigned(assetId);
        if (updated) {
            redirectAttributes.addFlashAttribute("message", "Asset status updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update asset status.");
        }
        return "redirect:/assets-requiring-service";
    }
    
    @GetMapping("/listwarranty")
    public String listWarrantyExpired(Model model) {
        List<Product> expiredProducts = prodService.getAllProducts().stream()
            .filter(product -> {
                LocalDate purchaseDate = product.getPurchaseDate().toLocalDate();
                LocalDate warrantyExpiryDate = purchaseDate.plusYears(product.getWarranty());
                return warrantyExpiryDate.isBefore(LocalDate.now());
            })
            .collect(Collectors.toList());

        model.addAttribute("expiredProducts", expiredProducts);
        return "warrantyExpired";
    }

    @GetMapping("/listassets")
    public String listAssets(Model model) {
        List<Asset> assets = assetService.listAll();
        
        List<Product> expiredProducts = assets.stream()
            .map(Asset::getProduct)
            .filter(product -> {
                LocalDate purchaseDate = product.getPurchaseDate().toLocalDate();
                LocalDate warrantyExpiryDate = purchaseDate.plusYears(product.getWarranty());
                return warrantyExpiryDate.isBefore(LocalDate.now());
            })
            .collect(Collectors.toList());

        model.addAttribute("assets", assets);
        model.addAttribute("expiredProducts", expiredProducts);
        return "listassets";
    }
    @GetMapping("/employee-dashboard")
    public String employeeDashboard() {
        return "employee-dashboard";
    }
    
    @GetMapping("/product")
    public String ProductDashboard() {
        return "product";
    }
    

    @GetMapping("/listproduct")
    public ModelAndView listProducts(Model model) {
        List<Product> products = prodService.getAllProducts();
        model.addAttribute("products", products);
        return new ModelAndView("listproduct");
    }
    
    @GetMapping("/newproduct")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("sellers", sellerService.listAll());
        return "newproduct";
    }

    @PostMapping("/saveproduct")
    public String saveProduct(@ModelAttribute @Valid ProductDTO productDTO,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            return "product-form";
        }
        System.out.println("Report1: " + productDTO.getReport1().getOriginalFilename());
        System.out.println("Report2: " + productDTO.getReport2().getOriginalFilename());
        System.out.println("Image: " + productDTO.getImage().getOriginalFilename());

        // Call service layer to process the productDTO
        prodService.createProduct(productDTO);

        // Redirect or show success message
        return "redirect:listproduct";
    }
    
    @GetMapping("/product/report/{productId}")
    public ResponseEntity<byte[]> viewReport1(@PathVariable Long productId) {
        System.out.println("Request received for report1 of Product ID: " + productId);
        byte[] report1 = prodService.getReport1ById(productId);
        if (report1 != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // Adjust content type if not PDF
            headers.setContentDispositionFormData("inline", "report1.pdf");
            return new ResponseEntity<>(report1, headers, HttpStatus.OK);
        } else {
            System.out.println("Report1 not found for Product ID: " + productId);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/product/image/{productId}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long productId) {
        byte[] imageBytes = prodService.getImageById(productId);
        System.out.println("Image bytes length: " + imageBytes.length);

        if (imageBytes != null && imageBytes.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on your image format
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    

    @GetMapping("product/edit/{productId}")
    public String editProductForm(@PathVariable Long productId, Model model) {
        ProductDTO productDTO = prodService.getProductDTOById(productId);
        model.addAttribute("product", productDTO); // "product" is the model attribute name
        return "editproduct"; // Return the name of your Thymeleaf template (edit-product.html)
    }

    @PostMapping("/updateproduct/{productId}")
    public String updateProduct(
            @PathVariable("productId") Long productId,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam("newReport1") MultipartFile newReport1,
            @RequestParam("newReport2") MultipartFile newReport2,
            @RequestParam("newImage") MultipartFile newImage,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            return "editproduct";
        }

        // Handle the new image file
        if (!newImage.isEmpty()) {
            try {
                byte[] imageBytes = newImage.getBytes();
                product.setImage(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Handle new reports similarly if needed
        if (!newReport1.isEmpty()) {
            try {
                byte[] report1Bytes = newReport1.getBytes();
                product.setReport1(report1Bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!newReport2.isEmpty()) {
            try {
                byte[] report2Bytes = newReport2.getBytes();
                product.setReport2(report2Bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        prodService.saveProduct(product);
        return "redirect:/listproduct";
    }

    @GetMapping("product/{productId}/delete")
    public String deleteProduct(@PathVariable Long productId) {
        // Logic to delete product
        prodService.deleteProduct(productId);
        return "listproduct";
    }
    
    @GetMapping("/seller")
    public String SellerDashboard() {
        return "seller";
    }
    
    
    @GetMapping("/newseller")
    public String showForm(Model model) {
      model.addAttribute("seller", new Seller());
      return "newseller";
    }
    
    @PostMapping("/savenewseller")
    public String createSeller(@ModelAttribute Seller seller) {
      sellerService.save(seller);
      return "redirect:/newseller";
    }
    
    @GetMapping("/listallseller")
    public String listSeller(Model model) {
        List<Seller> seller = sellerService.listAll();
        model.addAttribute("sellers", seller);
        return "listallseller";
    }
    
    @GetMapping("/seller/{SellerId}/edit")
    public ModelAndView showEditSellerPage(@PathVariable("SellerId") String SellerId) {
        ModelAndView mav = new ModelAndView("editseller"); 
        Seller seller = sellerService.getSellerById(SellerId); // Call the service method to get employee by email
        mav.addObject("seller", seller);
        return mav;
    }
    
    @GetMapping("/seller/{SellerId}/delete")
    public String deleteSeller(@PathVariable("SellerId") String SellerId) {
        // Delete employee from the database
        sellerService.deleteSeller(SellerId);
        return "redirect:/listallseller";
    }

    @PostMapping("/savesellerchanges")
    public String saveSellerChanges(@ModelAttribute("seller") Seller seller) {
    	Seller existingSeller = sellerService.getSellerById(seller.getSellerId());
        
        // Updating fields that are allowed to change
        existingSeller.setSellerId(seller.getSellerId());
        existingSeller.setCompanyName(seller.getCompanyName());
        existingSeller.setContactNo(seller.getContactNo());
        existingSeller.setEmailId(seller.getEmailId());
        existingSeller.setAddress(seller.getAddress());
        
        // Save the updated employee data
        sellerService.save(existingSeller);
        return "redirect:/listallseller"; 
    }
    
    @GetMapping("/addlocation")
    public String addLocationPage() {
        return "addlocation";
    }
    
    @GetMapping("/listlocation")
    public String listLocations(Model model) {
        List<Location> locations = locService.listAll();
        model.addAttribute("locations", locations);
        return "listlocation";
    }
    

    @GetMapping("/newlocation")
    public String newLocationPage() {
        return "newlocation";
    }

    @PostMapping("/savenewlocation")
    public String saveNewLocation(@RequestParam("location_name") String location_name,
                                  Model model) {
        Location location = new Location();
        location.setLocation_name(location_name);

        locService.save(location);

        return "redirect:/addlocation";
    }
    
    @GetMapping("/location/{locationId}/edit")
    public ModelAndView showEditlocationPage(@PathVariable("locationId") Long locationId) {
        ModelAndView mav = new ModelAndView("editlocation"); 
        Location location = locService.getLocationById(locationId); // Call the service method to get employee by email
        mav.addObject("location", location);
        return mav;
    }
    
    @PostMapping("/savelocationchanges")
    public String saveLocationChanges(@ModelAttribute("location") Location location) {
    	Location existingLocation = locService.getLocationById(location.getLocationId());
        
        // Updating fields that are allowed to change
        existingLocation.setLocationId(location.getLocationId());
        existingLocation.setLocation_name(location.getLocation_name());
        // Save the updated employee data
        locService.save(existingLocation);
        return "redirect:/listlocation"; 
    }
    
    @GetMapping("/location/{locationId}/delete")
    public String deleteLocation(@PathVariable("locationId") Long locationId) {
        // Delete employee from the database
        locService.deleteLocation(locationId);
        return "redirect:/listlocation";
    }
    
    @GetMapping("/addasset")
    public String addAssetPage() {
        return "addasset";
    }

    @GetMapping("/addnewasset")
    public String showAddAssetForm(Model model) {
    	List<Product> products = prodService.getAllProducts();
    	List<Employee> emp = empService.listAll();// Fetch the list of products from the database
        model.addAttribute("products", products);
        model.addAttribute("emp", emp);
        model.addAttribute("asset", new Asset());
        return "addnewasset"; // Thymeleaf template name
    }

    // Mapping to handle form submission and save asset
//    @PostMapping("/saveasset")
//    public String saveAsset(@ModelAttribute("asset") Asset asset) {
//        assetService.save(asset);
//        return "redirect:/listassets"; // Redirect to the add asset form after saving
//    }
    
    @PostMapping("/saveasset")
    public String saveAsset(@ModelAttribute("asset") Asset asset) {
        // Fetch the product from the database using the productId
        Long productId = asset.getProduct().getProductId();
        Product product = prodService.getProductById(productId);
        
        // Fetch the employee from the database using the employeeId
        Long employeeId = asset.getEmployee().getEmployeeId();
        Employee employee = empService.getByEmployeeId(employeeId);

        if (product != null && employee != null) {
            // Set the fetched product and employee in the asset
            asset.setProduct(product);
            asset.setEmployee(employee);
            
            // Save the asset
            assetService.save(asset);
            return "redirect:/listassets"; // Redirect to the list assets page after saving
        } else {
            // Handle the case where the product or employee is not found
            // You could either return an error message or redirect to an error page
            return "redirect:/error";
        }
    }

    // Mapping to fetch all assets and display them
//    @GetMapping("/listassets")
//    public String showAllAssets(Model model) {
//        List<Asset> assets = assetService.listAll();
//        model.addAttribute("assets", assets);
//        return "listassets"; // Thymeleaf template name
//    }
    
    @GetMapping("/downloadassets")
    public ResponseEntity<byte[]> downloadAssets() {
        List<Asset> assets = assetService.listAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Write CSV headers
        writer.println("Asset ID,Product ID,Product Name,Employee ID,Employee Name,Location ID,Status");

        // Write asset data
        for (Asset asset : assets) {
            writer.println(String.format("%d,%d,%s,%d,%s,%d,%s",
                    asset.getAssetId(),
                    asset.getProduct().getProductId(),
                    asset.getProduct().getProductName(),
                    asset.getEmployee().getEmployeeId(),
                    asset.getEmployee().getName(),
                    asset.getLocationId(),
                    asset.getStatus()));
        }

        writer.flush();

        byte[] data = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "assets.csv");
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
    
    @PostMapping("/asset/{id}/service")
    public String sendForServicing(@PathVariable("id") Long assetId, RedirectAttributes redirectAttributes) {
        try {
            assetService.updateStatus(assetId, "Servicing Required");
            redirectAttributes.addFlashAttribute("message", "Asset sent for servicing successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error sending asset for servicing: " + e.getMessage());
        }
        return "redirect:/listassets"; // Redirect to the assets list page
    }
    
    @GetMapping("/downloadassets/pdf")
    public ResponseEntity<byte[]> downloadAssetsPdf() {
        List<Asset> assets = assetService.listAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Assets List"));

        Table table = new Table(new float[]{1, 1, 1, 1, 1, 1, 1});
        table.addCell("Asset ID");
        table.addCell("Product ID");
        table.addCell("Product Name");
        table.addCell("Employee ID");
        table.addCell("Employee Name");
        table.addCell("Location ID");
        table.addCell("Status");

        for (Asset asset : assets) {
            table.addCell(String.valueOf(asset.getAssetId()));
            table.addCell(String.valueOf(asset.getProduct().getProductId()));
            table.addCell(asset.getProduct().getProductName());
            table.addCell(String.valueOf(asset.getEmployee().getEmployeeId()));
            table.addCell(asset.getEmployee().getName());
            table.addCell(String.valueOf(asset.getLocationId()));
            table.addCell(asset.getStatus());
        }

        document.add(table);
        document.close();

        byte[] data = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "assets.pdf");
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
    
    @GetMapping("/product/presentIn/{productId}")
    public String getProductLocations(@PathVariable Long productId, Model model) {
        List<Location> locations = assetService.getProductLocations(productId);
        model.addAttribute("locations", locations);
        return "presentIn";
    }
    @PostMapping("/product/presentIn/{productId}/generate-pdf")
    public void generatePdf(@PathVariable Long productId, HttpServletResponse response) throws IOException {
        // Retrieve product, location, and asset data
        Product product = prodService.getProductById(productId);  // Fetch product details
        if (product == null) {
            System.out.println("No product found with ID: " + productId);
            return;
        }
        
        List<Location> locations = assetService.getProductLocations(productId);  // Get locations for the product
        if (locations == null || locations.isEmpty()) {
            System.out.println("No locations found for product ID: " + productId);
            return;
        }
        
        List<Asset> assets = assetService.listAll();  // Get all assets to filter below
        if (assets == null || assets.isEmpty()) {
            System.out.println("No assets found.");
            return;
        }

        // Count the assigned assets for the product
        long assignedAssetsCount = assets.stream()
                .filter(asset -> asset.getProduct().getProductId().equals(productId))
                .count();
        
        // Calculate the quantity left
        int quantityLeft = product.getQuantity() - (int) assignedAssetsCount;

        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Product_Locations_" + productId + ".pdf");

        // Create the PDF document
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdf);
//        
//        String imagePath = "\"C:\\Users\\abidi\\OneDrive\\Desktop\\logo.jpg\""; // Path to the company logo
//        ImageData imageData = ImageDataFactory.create(imagePath);
//        Image logo = new Image(imageData);
//        logo.setWidth(100);
//        document.add(logo);
//        
        document.add(new Paragraph("Product Location Report")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Generated on: " + java.time.LocalDate.now())
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" ")
                .setFontSize(12));


        // Add product information
        document.add(new Paragraph("Product Information").setBold().setFontSize(16));
        document.add(new Paragraph("Product ID: " + productId));
        document.add(new Paragraph("Product Name: " + product.getProductName()));
        document.add(new Paragraph("Seller ID: " + product.getSellerId()));
        document.add(new Paragraph("Quantity Left: " + quantityLeft));
        document.add(new Paragraph(" "));

        // Create the table with columns for serial number, location, and asset details
        Table table = new Table(new float[]{1, 1, 2, 1, 2, 2});
        table.addHeaderCell(new Cell().add(new Paragraph("Serial No")));
        table.addHeaderCell(new Cell().add(new Paragraph("Location ID")));
        table.addHeaderCell(new Cell().add(new Paragraph("Location Name")));
        table.addHeaderCell(new Cell().add(new Paragraph("Asset ID")));
        table.addHeaderCell(new Cell().add(new Paragraph("Asset Status")));
        table.addHeaderCell(new Cell().add(new Paragraph("Product Name")));

        // Initialize serial number
        int serialNumber = 1;

        // Populate the table with location and asset data
        for (Location location : locations) {
            System.out.println("Processing location ID: " + location.getLocationId());
            List<Asset> locationAssets = assets.stream()
                .filter(asset -> asset.getLocationId().equals(location.getLocationId()) && asset.getProduct().getProductId().equals(productId))
                .collect(Collectors.toList());

            if (locationAssets.isEmpty()) {
                System.out.println("No assets found for location ID: " + location.getLocationId());
                continue;
            }

            for (Asset asset : locationAssets) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(serialNumber++))));
                table.addCell(new Cell().add(new Paragraph(location.getLocationId().toString())));
                table.addCell(new Cell().add(new Paragraph(location.getLocation_name())));
                table.addCell(new Cell().add(new Paragraph(asset.getAssetId().toString())));
                table.addCell(new Cell().add(new Paragraph(asset.getStatus())));
                table.addCell(new Cell().add(new Paragraph(product.getProductName())));
            }
        }

        // Add the table to the document
        document.add(table);
        
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new IEventHandler() {
            @Override
            public void handleEvent(Event event) {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                com.itextpdf.kernel.pdf.PdfDocument pdfDoc = docEvent.getDocument();
                int pageNumber = pdfDoc.getPageNumber(pdfDoc.getLastPage());
                Rectangle pageSize = pdfDoc.getDefaultPageSize();
                Canvas canvas = new Canvas(pdfDoc.getPage(pageNumber), pageSize);
                canvas.showTextAligned(new Paragraph(String.format("Page %d", pageNumber)),
                        pageSize.getWidth() / 2, pageSize.getBottom() + 30, TextAlignment.CENTER);
            }
        });

        // Close the document
        document.close();
    }

    @PostMapping("/product/presentIn/{productId}/generate-total-quantity-report")
    public void generateTotalQuantityReport(@PathVariable Long productId, HttpServletResponse response) throws IOException {
        Product product = prodService.getProductById(productId);
        if (product == null) {
            System.out.println("No product found with ID: " + productId);
            return;
        }

        List<Location> locations = assetService.getProductLocations(productId);
        if (locations == null || locations.isEmpty()) {
            System.out.println("No locations found for product ID: " + productId);
            return;
        }

        List<Asset> assets = assetService.listAll();
        if (assets == null || assets.isEmpty()) {
            System.out.println("No assets found.");
            return;
        }

        long assignedAssetsCount = assets.stream()
                .filter(asset -> asset.getProduct().getProductId().equals(productId))
                .count();

        int quantityLeft = product.getQuantity() - (int) assignedAssetsCount;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Total_Quantity_Report_" + productId + ".pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4, false);
        document.setMargins(40, 20, 40, 20); // Adjust margins as needed

        // Create a header table for the logo and date
        Table headerTable = new Table(new float[]{1, 2})
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(20);

        // Add larger logo on the left
        String logoPath = "src/main/resources/static/logo.jpg"; // Change this to your logo path
        Image logo = new Image(ImageDataFactory.create(logoPath));
        logo.setWidth(180); // Adjust width as needed
        logo.setHeight(100); // Ensure height is consistent
        logo.setHorizontalAlignment(HorizontalAlignment.LEFT);
        Cell logoCell = new Cell().add(logo).setBorder(Border.NO_BORDER);
        headerTable.addCell(logoCell);

        // Add date on the right
        Cell dateCell = new Cell().add(new Paragraph("Generated on: " + java.time.LocalDate.now())
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(20))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Border.NO_BORDER);
        headerTable.addCell(dateCell);

        document.add(headerTable);

        // Add title with improved styling
        Paragraph title = new Paragraph("Total Quantity Report")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(28)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(10)
                .setBackgroundColor(ColorConstants.DARK_GRAY) // Background color
                .setBorder(new SolidBorder(ColorConstants.BLUE, 2)) // Border around the title
                .setMarginBottom(30);

        // Shadow effect
        Div titleDiv = new Div()
                .setBorder(new SolidBorder(ColorConstants.DARK_GRAY, 2))
                .setPadding(5)
                .setBackgroundColor(ColorConstants.GRAY);

        titleDiv.add(title);
        document.add(titleDiv);

        // Add product information section in a box with improved styling
        Div productInfoDiv = new Div()
                .setBorder(new SolidBorder(ColorConstants.GRAY, 1))
                .setPadding(10)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY) // Add background color
                .setFontSize(10); // Smaller font size for product info

        productInfoDiv.add(new Paragraph("Product Information")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(16)
                .setMarginBottom(10)
                .setTextAlignment(TextAlignment.CENTER)); // Center align heading

        productInfoDiv.add(new Paragraph("Product ID: " + productId)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setMarginBottom(5));
        productInfoDiv.add(new Paragraph("Product Name: " + product.getProductName())
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setMarginBottom(5));
        productInfoDiv.add(new Paragraph("Seller ID: " + product.getSellerId())
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setMarginBottom(5));
        productInfoDiv.add(new Paragraph("Quantity Left: " + quantityLeft)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setMarginBottom(10));

        document.add(productInfoDiv);

        // Add table with improved styling
        Table table = new Table(new float[]{1, 2, 2})
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(20);

        // Table headers with improved styling
        table.addHeaderCell(new Cell().add(new Paragraph("Location ID")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY))); // Add background color for header
        table.addHeaderCell(new Cell().add(new Paragraph("Location Name")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new Paragraph("Number of Assets")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)));

        // Table content
        for (Location location : locations) {
            List<Asset> locationAssets = assets.stream()
                    .filter(asset -> asset.getLocationId().equals(location.getLocationId()) && asset.getProduct().getProductId().equals(productId))
                    .collect(Collectors.toList());

            table.addCell(new Cell().add(new Paragraph(location.getLocationId().toString())
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))));
            table.addCell(new Cell().add(new Paragraph(location.getLocation_name())
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(locationAssets.size()))
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))));
        }

        document.add(table);

        // Add footer with page numbers
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new IEventHandler() {
            @Override
            public void handleEvent(Event event) {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfDocument pdfDoc = docEvent.getDocument();
                int pageNumber = pdfDoc.getPageNumber(docEvent.getPage());
                Rectangle pageSize = pdfDoc.getPage(pageNumber).getPageSize();
                float x = pageSize.getRight() - 30;
                float y = pageSize.getBottom() + 20;
                Canvas canvas = new Canvas(pdfDoc.getPage(pageNumber), new Rectangle(x - 20, y, 40, 20));
                try {
					canvas.showTextAligned(new Paragraph(String.format("Page %d", pageNumber))
					        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
					        .setFontSize(10), // Smaller font size for footer
					        x, y, TextAlignment.RIGHT);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                canvas.close();
            }
        });

        document.close();
    }

    @GetMapping("/product/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        List<Product> products = prodService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "listproduct";
    }
    
    @GetMapping("/downloadasset")
    public ResponseEntity<byte[]> downloadAssetss() {
        List<Asset> assets = assetService.listAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Write CSV headers
        writer.println("Asset ID,Product ID,Product Name,Employee ID,Employee Name,Location ID,Status");

        // Write asset data
        for (Asset asset : assets) {
            writer.println(String.format("%d,%d,%s,%d,%s,%d,%s",
                    asset.getAssetId(),
                    asset.getProduct().getProductId(),
                    asset.getProduct().getProductName(),
                    asset.getEmployee().getEmployeeId(),
                    asset.getEmployee().getName(),
                    asset.getLocationId(),
                    asset.getStatus()));
        }

        writer.flush();

        byte[] data = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "assets.csv");
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    // Mapping to download assets as PDF
    @GetMapping("/downloadasset/pdf")
    public ResponseEntity<byte[]> downloadAssetsPdff() {
        List<Asset> assets = assetService.listAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Assets List"));

        Table table = new Table(new float[]{1, 1, 1, 1, 1, 1, 1});
        table.addCell("Asset ID");
        table.addCell("Product ID");
        table.addCell("Product Name");
        table.addCell("Employee ID");
        table.addCell("Employee Name");
        table.addCell("Location ID");
        table.addCell("Status");

        for (Asset asset : assets) {
            table.addCell(String.valueOf(asset.getAssetId()));
            table.addCell(String.valueOf(asset.getProduct().getProductId()));
            table.addCell(asset.getProduct().getProductName());
            table.addCell(String.valueOf(asset.getEmployee().getEmployeeId()));
            table.addCell(asset.getEmployee().getName());
            table.addCell(String.valueOf(asset.getLocationId()));
            table.addCell(asset.getStatus());
        }

        document.add(table);
        document.close();

        byte[] data = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "assets.pdf");
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
    
    @GetMapping("/product/{productId}/generate-pdf")
    public ResponseEntity<byte[]> downloadProductPdff() {
        List<Product> prods = prodService.getAllProducts();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Product Description"));

        Table table = new Table(new float[]{1, 1, 1});
        table.addCell("Product ID");
        table.addCell("Product Name");
        table.addCell("Seller Id");
      
        for (Product prod : prods) {
            table.addCell(String.valueOf(prod.getProductId()));
            table.addCell(String.valueOf(prod.getProductName()));
            table.addCell(String.valueOf(prod.getSellerId()));
           
        }

        document.add(table);
        document.close();

        byte[] data = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "productInfo.pdf");
        headers.setContentLength(data.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
    
    
    @GetMapping("/asset/{assetId}/edit")
    public String showEditAssetPage(@PathVariable("assetId") Long assetId, Model model) {
    	Asset asset = assetService.getAssetById(assetId);
        model.addAttribute("asset", asset);
        model.addAttribute("product", asset.getProduct());
        model.addAttribute("locationId", asset.getLocationId());
        model.addAttribute("employee", asset.getEmployee());
        model.addAttribute("status", asset.getStatus());
        // Add any other necessary model attributes
        return "editasset"; // Thymeleaf template name for editing asset
    }

    // Mapping to save edited asset changes
    @PostMapping("/updateasset/{id}")
    public String saveAssetChanges(@PathVariable("assetId") Long assetId,@ModelAttribute("asset") Asset asset) {
        Asset existingAsset = assetService.getAssetById(assetId);

        // Update fields that are allowed to change
        existingAsset.setAssetId(asset.getAssetId());
        existingAsset.setProduct(asset.getProduct());
        existingAsset.setEmployee(asset.getEmployee());
        existingAsset.setLocationId(asset.getLocationId());
        existingAsset.setStatus(asset.getStatus());

        // Save updated asset data
        assetService.save(existingAsset);

        return "redirect:/listassets"; // Redirect to list assets page after saving changes
    }

//     Mapping to delete an asset
    @GetMapping("/asset/{assetId}/delete")
    public String deleteAsset(@PathVariable("assetId") Long assetId) {
        assetService.deleteAsset(assetId);
        return "redirect:/listassets"; // Redirect to list assets page after deletion
    }
    
    @GetMapping("/location/{locationId}/generate-report")
    public void generateLocationAssetReport(@PathVariable Long locationId, HttpServletResponse response) throws IOException {
        Location location = locService.getLocationById(locationId);
        if (location == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location not found");
            return;
        }

        List<Asset> assets = assetService.getAssetsByLocationId(locationId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Location_Asset_Report_" + locationId + ".pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        document.add(new Paragraph("Asset Report for Location ID: " + locationId)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(18));

        document.add(new Paragraph("Location Name: " + location.getLocation_name())
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14));

        Table table = new Table(new float[]{1, 2, 2, 2, 2, 1, 1});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Paragraph("Asset ID").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        table.addHeaderCell(new Paragraph("Product ID").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        table.addHeaderCell(new Paragraph("Product Name").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        table.addHeaderCell(new Paragraph("Employee ID").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        table.addHeaderCell(new Paragraph("Employee Name").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        table.addHeaderCell(new Paragraph("Status").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        table.addHeaderCell(new Paragraph("Location ID").setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

        for (Asset asset : assets) {
            table.addCell(new Paragraph(asset.getAssetId().toString()).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));
            table.addCell(new Paragraph(asset.getProduct().getProductId().toString()).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));
            table.addCell(new Paragraph(asset.getProduct().getProductName().toString()).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));
            table.addCell(new Paragraph(asset.getEmployee().getEmployeeId().toString()).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));
            table.addCell(new Paragraph(asset.getEmployee().getName().toString()).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));
            table.addCell(new Paragraph(asset.getStatus()).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));
            table.addCell(new Paragraph(asset.getLocationId().toString()).setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA)));
        }

        document.add(table);
        document.close();
    }
}

