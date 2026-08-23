package com.example.Emp.Repository;

import com.example.Emp.Entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    Page<Employee> findByDepartment(String department , Pageable pageable);

    Page<Employee> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<Employee> findBySalaryGreaterThan(Double salary,Pageable pageable);

    // Bonus: case-insensitive partial name search

}
