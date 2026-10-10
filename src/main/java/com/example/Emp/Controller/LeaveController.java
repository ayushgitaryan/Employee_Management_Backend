package com.example.Emp.Controller;

import com.example.Emp.Service.LeaveService;
import com.example.Emp.dto.LeaveRequestDTO;
import com.example.Emp.dto.LeaveSummaryDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/me")
    public ResponseEntity<LeaveSummaryDTO> getMyLeaveSummary(Principal principal) {
        LeaveSummaryDTO summary = leaveService.getLeaveSummary(principal.getName());
        return ResponseEntity.ok(summary);
    }

    @PostMapping
    public ResponseEntity<LeaveRequestDTO> applyLeave(Principal principal, @Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequestDTO created = leaveService.applyLeave(principal.getName(), leaveRequestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelLeave(Principal principal, @PathVariable Long id) {
        leaveService.cancelLeave(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
