package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.domain.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
	 Employee findByEmployeeIdAndPassword(Long employeeId, String password);
	 void deleteByEmployeeId(Long employeeId);
	 Employee findByEmployeeId(Long employeeId);
}
