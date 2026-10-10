package com.example.Emp.Service;

import com.example.Emp.Entity.Employee;
import com.example.Emp.Entity.Payroll;
import com.example.Emp.Repository.PayrollRepository;
import com.example.Emp.dto.PayrollDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeService employeeService;

    @Autowired
    public PayrollServiceImpl(PayrollRepository payrollRepository, EmployeeService employeeService) {
        this.payrollRepository = payrollRepository;
        this.employeeService = employeeService;
    }

    @Override
    public List<PayrollDTO> getPayrollHistory(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);

        List<Payroll> payrolls = payrollRepository.findByEmployeeOrderByPaymentDateDesc(employee);

        if (payrolls.isEmpty()) {
            // Seed a current month payroll record if none exists yet
            Double baseSalary = employee.getSalary() != null ? employee.getSalary() : 65000.0;
            Double netSalary = baseSalary * 0.85; // after standard deductions

            Payroll defaultPayroll = new Payroll(
                    employee,
                    LocalDate.now().getMonth().name() + " " + LocalDate.now().getYear(),
                    baseSalary,
                    netSalary,
                    LocalDate.now().withDayOfMonth(1),
                    "PAID"
            );

            payrollRepository.save(defaultPayroll);
            payrolls = List.of(defaultPayroll);
        }

        return payrolls.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PayrollDTO getLatestPayroll(String username) {
        List<PayrollDTO> history = getPayrollHistory(username);
        return history.isEmpty() ? null : history.get(0);
    }

    private PayrollDTO mapToDTO(Payroll payroll) {
        return new PayrollDTO(
                payroll.getId(),
                payroll.getEmployee().getId(),
                payroll.getPayPeriod(),
                payroll.getBasicSalary(),
                payroll.getNetSalary(),
                payroll.getPaymentDate(),
                payroll.getStatus()
        );
    }
}
