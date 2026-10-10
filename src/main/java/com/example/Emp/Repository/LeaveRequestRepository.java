package com.example.Emp.Repository;

import com.example.Emp.Entity.Employee;
import com.example.Emp.Entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeOrderByAppliedOnDesc(Employee employee);
    long countByEmployeeAndStatus(Employee employee, String status);
}
