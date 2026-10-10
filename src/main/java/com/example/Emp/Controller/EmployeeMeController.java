package com.example.Emp.Controller;

import com.example.Emp.Service.EmployeeService;
import com.example.Emp.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/employee/me")
public class EmployeeMeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeMeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<EmployeeDTO> getMyProfile(Principal principal) {
        String username = principal.getName();
        EmployeeDTO profile = employeeService.getEmployeeByUsername(username);
        return ResponseEntity.ok(profile);
    }
}
