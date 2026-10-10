package com.example.Emp.Repository;

import com.example.Emp.Entity.Employee;
import com.example.Emp.Entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeOrderByPaymentDateDesc(Employee employee);
}
