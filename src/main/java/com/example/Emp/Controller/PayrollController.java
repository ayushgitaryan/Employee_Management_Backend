package com.example.Emp.Controller;

import com.example.Emp.Service.PayrollService;
import com.example.Emp.dto.PayrollDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    @Autowired
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<PayrollDTO>> getMyPayrollHistory(Principal principal) {
        List<PayrollDTO> history = payrollService.getPayrollHistory(principal.getName());
        return ResponseEntity.ok(history);
    }
}
