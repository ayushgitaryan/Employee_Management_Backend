package com.example.Emp.Controller;

import com.example.Emp.Service.NotificationService;
import com.example.Emp.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<NotificationDTO>> getMyNotifications(Principal principal) {
        List<NotificationDTO> notifications = notificationService.getNotifications(principal.getName());
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(Principal principal, @PathVariable Long id) {
        notificationService.markAsRead(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
