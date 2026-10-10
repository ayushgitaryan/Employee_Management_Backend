package com.example.Emp.Controller;

import com.example.Emp.Service.AttendanceService;
import com.example.Emp.dto.AttendanceDTO;
import com.example.Emp.dto.AttendanceSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDTO> checkIn(Principal principal) {
        AttendanceDTO attendance = attendanceService.checkIn(principal.getName());
        return new ResponseEntity<>(attendance, HttpStatus.CREATED);
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceDTO> checkOut(Principal principal) {
        AttendanceDTO attendance = attendanceService.checkOut(principal.getName());
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/me")
    public ResponseEntity<AttendanceDTO> getTodayAttendance(Principal principal) {
        AttendanceDTO attendance = attendanceService.getTodayAttendance(principal.getName());
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/me/history")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceHistory(Principal principal) {
        List<AttendanceDTO> history = attendanceService.getAttendanceHistory(principal.getName());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/me/summary")
    public ResponseEntity<AttendanceSummaryDTO> getMonthlySummary(Principal principal) {
        AttendanceSummaryDTO summary = attendanceService.getMonthlySummary(principal.getName());
        return ResponseEntity.ok(summary);
    }
}
