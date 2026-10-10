package com.example.Emp.Service;

import com.example.Emp.Entity.Attendance;
import com.example.Emp.Entity.Employee;
import com.example.Emp.Repository.AttendanceRepository;
import com.example.Emp.dto.AttendanceDTO;
import com.example.Emp.dto.AttendanceSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeService employeeService) {
        this.attendanceRepository = attendanceRepository;
        this.employeeService = employeeService;
    }

    @Override
    public AttendanceDTO checkIn(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);
        LocalDate today = LocalDate.now();

        Optional<Attendance> existing = attendanceRepository.findByEmployeeAndDate(employee, today);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Employee has already checked in for today (" + today + ")");
        }

        Attendance attendance = new Attendance(
                employee,
                today,
                LocalTime.now(),
                null,
                "PRESENT"
        );

        Attendance saved = attendanceRepository.save(attendance);
        return mapToDTO(saved);
    }

    @Override
    public AttendanceDTO checkOut(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, today)
                .orElseThrow(() -> new IllegalArgumentException("No check-in record found for today. Please check in first."));

        if (attendance.getCheckOutTime() != null) {
            throw new IllegalArgumentException("Employee has already checked out for today.");
        }

        attendance.setCheckOutTime(LocalTime.now());
        attendance.setStatus("PRESENT");
        Attendance saved = attendanceRepository.save(attendance);
        return mapToDTO(saved);
    }

    @Override
    public AttendanceDTO getTodayAttendance(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);
        LocalDate today = LocalDate.now();
        return attendanceRepository.findByEmployeeAndDate(employee, today)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public List<AttendanceDTO> getAttendanceHistory(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);
        return attendanceRepository.findByEmployeeOrderByDateDesc(employee)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceSummaryDTO getMonthlySummary(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        long presentDays = attendanceRepository.countByEmployeeAndDateBetweenAndStatus(employee, startDate, endDate, "PRESENT");
        long totalDaysInMonth = currentMonth.lengthOfMonth();

        return new AttendanceSummaryDTO(
                currentMonth.getMonth().name() + " " + currentMonth.getYear(),
                presentDays,
                totalDaysInMonth - presentDays,
                totalDaysInMonth
        );
    }

    private AttendanceDTO mapToDTO(Attendance attendance) {
        return new AttendanceDTO(
                attendance.getId(),
                attendance.getEmployee().getId(),
                attendance.getDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getStatus()
        );
    }
}
