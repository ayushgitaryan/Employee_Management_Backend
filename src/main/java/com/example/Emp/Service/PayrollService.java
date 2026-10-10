package com.example.Emp.Service;

import com.example.Emp.dto.PayrollDTO;
import java.util.List;

public interface PayrollService {
    List<PayrollDTO> getPayrollHistory(String username);
    PayrollDTO getLatestPayroll(String username);
}
