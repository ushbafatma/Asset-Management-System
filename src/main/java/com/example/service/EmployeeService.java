package com.example.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.domain.Employee;
import com.example.repository.EmployeeRepo;


import jakarta.transaction.Transactional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo repo;

    public Employee login(Long employeeId, String password) {
        // Add logging for received credentials
        System.out.println("Checking login for employee ID: " + employeeId);

        // Example repository method call
        Employee employee = repo.findByEmployeeIdAndPassword(employeeId, password);

        if (employee != null) {
            System.out.println("Employee found: " + employee.getEmployeeId());
        } else {
            System.out.println("No employee found with the given credentials");
        }

        return employee;
    }
    
    
    @Transactional
    public void save(Employee std) {
        repo.save(std);
    }
    
    public List<Employee> listAll() {
        return repo.findAll();
    }
    
    @Transactional
    public void deleteByEmployeeId(Long employeeId) {
        repo.deleteByEmployeeId(employeeId);
    }
    public Employee getByEmployeeId(Long employeeId) {
        return repo.findByEmployeeId(employeeId);
    }
    public long countEmployees() {
        return repo.count();
    }
    public void saveAll(List<Employee> employees) {
        repo.saveAll(employees);
    }
//    public void saveEmployeesFromFile(MultipartFile file) throws IOException, CsvValidationException, ParseException {
//        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
//            String[] values;
//            List<Employee> employees = new ArrayList<>();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//            // Skip the header
//            csvReader.readNext();
//
//            while ((values = csvReader.readNext()) != null) {
//                Employee employee = new Employee();
//                employee.setName(values[0]);
//                employee.setDate_of_joining(new Date(dateFormat.parse(values[1]).getTime())); // Convert java.util.Date to java.sql.Date
//                employee.setPassword(values[2]);
//                employee.setPhone_no(values[3]);
//                employee.setRole(values[4]);
//                employees.add(employee);
//            }
//
//            repo.saveAll(employees);
//        }
//    }

}
