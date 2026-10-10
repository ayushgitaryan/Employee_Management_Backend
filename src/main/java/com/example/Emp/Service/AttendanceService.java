package com.example.Emp.Service;

import com.example.Emp.dto.AttendanceDTO;
import com.example.Emp.dto.AttendanceSummaryDTO;
import java.util.List;

public interface AttendanceService {
    AttendanceDTO checkIn(String username);
    AttendanceDTO checkOut(String username);
    AttendanceDTO getTodayAttendance(String username);
    List<AttendanceDTO> getAttendanceHistory(String username);
    AttendanceSummaryDTO getMonthlySummary(String username);
}
