package com.example.Emp.dto;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private Long employeeId;
    private String title;
    private String message;
    private String type;
    private LocalDateTime createdAt;
    private boolean read;

    public NotificationDTO() {
    }

    public NotificationDTO(Long id, Long employeeId, String title, String message, String type, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.employeeId = employeeId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.read = read;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
