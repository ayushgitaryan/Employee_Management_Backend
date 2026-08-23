package com.example.Emp.Controller;

import com.example.Emp.Service.EmployeeService;
import com.example.Emp.dto.EmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // POST /api/employees -> Create a new employee
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid  @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    // GET /api/employees -> Get all employees
    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Double minSalary,
            Pageable pageable) {

        Page<EmployeeDTO> employees;

        if (department != null || firstName != null || minSalary != null) {
            employees = employeeService.searchEmployees(department, firstName, minSalary, pageable);
        } else {
            employees = employeeService.getAllEmployees(pageable);
        }

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // GET /api/employees/{id} -> Get one employee by id
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    // PUT /api/employees/{id} -> Update an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    // DELETE /api/employees/{id} -> Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
