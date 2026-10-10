package com.example.Emp.Repository;

import com.example.Emp.Entity.Attendance;
import com.example.Emp.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);
    List<Attendance> findByEmployeeOrderByDateDesc(Employee employee);
    List<Attendance> findByEmployeeAndDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);
    long countByEmployeeAndDateBetweenAndStatus(Employee employee, LocalDate startDate, LocalDate endDate, String status);
}
