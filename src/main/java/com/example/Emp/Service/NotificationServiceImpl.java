package com.example.Emp.Service;

import com.example.Emp.Entity.Employee;
import com.example.Emp.Entity.Notification;
import com.example.Emp.Repository.NotificationRepository;
import com.example.Emp.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeService employeeService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, EmployeeService employeeService) {
        this.notificationRepository = notificationRepository;
        this.employeeService = employeeService;
    }

    @Override
    public List<NotificationDTO> getNotifications(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);

        List<Notification> notifications = notificationRepository.findByEmployeeOrderByCreatedAtDesc(employee);

        if (notifications.isEmpty()) {
            // Create welcome & announcement notification if empty
            Notification welcome = new Notification(
                    employee,
                    "Welcome to Employee Portal",
                    "Your employee account is active. Explore your dashboard to track attendance, manage leaves, and view payslips.",
                    "ANNOUNCEMENT",
                    LocalDateTime.now(),
                    false
            );
            notificationRepository.save(welcome);
            notifications = List.of(welcome);
        }

        return notifications.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String username, Long notificationId) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + notificationId));

        if (!notification.getEmployee().getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Forbidden: Cannot modify another employee's notification.");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void createNotification(Employee employee, String title, String message, String type) {
        Notification notification = new Notification(
                employee,
                title,
                message,
                type,
                LocalDateTime.now(),
                false
        );
        notificationRepository.save(notification);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getEmployee().getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getCreatedAt(),
                notification.isRead()
        );
    }
}
