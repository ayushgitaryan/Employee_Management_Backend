package com.example.Emp.Service;

import com.example.Emp.Entity.Employee;
import com.example.Emp.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotifications(String username);
    void markAsRead(String username, Long notificationId);
    void createNotification(Employee employee, String title, String message, String type);
}
