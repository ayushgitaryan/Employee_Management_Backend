package com.example.Emp.Service;

import com.example.Emp.dto.EmployeeDTO;
import com.example.Emp.Entity.Employee;
import com.example.Emp.exception.EmployeeNotFoundException;
import com.example.Emp.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Employee with this email already exists: " + employeeDTO.getEmail());
        }

        Employee employee = mapToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return mapToDTO(employee);
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(this::mapToDTO);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        existingEmployee.setFirstName(employeeDTO.getFirstName());
        existingEmployee.setLastName(employeeDTO.getLastName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setPhone(employeeDTO.getPhone());
        existingEmployee.setDepartment(employeeDTO.getDepartment());
        existingEmployee.setSalary(employeeDTO.getSalary());
        existingEmployee.setJoiningDate(employeeDTO.getJoiningDate());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapToDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    // ---- Helper methods: Entity <-> DTO mapping ----

    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartment(employee.getDepartment());
        dto.setSalary(employee.getSalary());
        dto.setJoiningDate(employee.getJoiningDate());
        return dto;
    }

    private Employee mapToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setJoiningDate(dto.getJoiningDate());
        return employee;
    }
    @Override
    public Page<EmployeeDTO> searchEmployees(String department, String firstName, Double minSalary, Pageable pageable) {
        Page<Employee> employeePage;

        if (department != null) {
            employeePage = employeeRepository.findByDepartment(department, pageable);
        } else if (firstName != null) {
            employeePage = employeeRepository.findByFirstNameContainingIgnoreCase(firstName, pageable);
        } else if (minSalary != null) {
            employeePage = employeeRepository.findBySalaryGreaterThan(minSalary, pageable);
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }

        return employeePage.map(this::mapToDTO);
    }
}