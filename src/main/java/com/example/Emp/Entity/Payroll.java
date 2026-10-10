package com.example.Emp.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payrolls")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private String payPeriod; // e.g. "August 2026"
    private Double basicSalary;
    private Double netSalary;
    private LocalDate paymentDate;
    private String status; // PAID, PROCESSING

    public Payroll() {
    }

    public Payroll(Employee employee, String payPeriod, Double basicSalary, Double netSalary, LocalDate paymentDate, String status) {
        this.employee = employee;
        this.payPeriod = payPeriod;
        this.basicSalary = basicSalary;
        this.netSalary = netSalary;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

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
