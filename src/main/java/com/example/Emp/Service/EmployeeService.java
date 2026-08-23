package com.example.Emp.Service;

import com.example.Emp.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO getEmployeeById(Long id);

    Page<EmployeeDTO> getAllEmployees(Pageable pageable);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    Page<EmployeeDTO> searchEmployees(String department, String firstName, Double minSalary, Pageable pageable);

    void deleteEmployee(Long id);
}