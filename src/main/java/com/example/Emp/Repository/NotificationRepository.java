package com.example.Emp.Repository;

import com.example.Emp.Entity.Employee;
import com.example.Emp.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEmployeeOrderByCreatedAtDesc(Employee employee);
}
