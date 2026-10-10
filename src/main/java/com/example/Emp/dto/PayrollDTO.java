package com.example.Emp.dto;

import java.time.LocalDate;

public class PayrollDTO {
    private Long id;
    private Long employeeId;
    private String payPeriod;
    private Double basicSalary;
    private Double netSalary;
    private LocalDate paymentDate;
    private String status;

    public PayrollDTO() {
    }

    public PayrollDTO(Long id, Long employeeId, String payPeriod, Double basicSalary, Double netSalary, LocalDate paymentDate, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.payPeriod = payPeriod;
        this.basicSalary = basicSalary;
        this.netSalary = netSalary;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getPayPeriod() { return payPeriod; }
    public void setPayPeriod(String payPeriod) { this.payPeriod = payPeriod; }

    public Double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Double basicSalary) { this.basicSalary = basicSalary; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
