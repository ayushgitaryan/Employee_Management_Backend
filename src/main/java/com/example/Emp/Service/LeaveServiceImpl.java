package com.example.Emp.Service;

import com.example.Emp.Entity.Employee;
import com.example.Emp.Entity.LeaveRequest;
import com.example.Emp.Repository.LeaveRequestRepository;
import com.example.Emp.dto.LeaveRequestDTO;
import com.example.Emp.dto.LeaveSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeService employeeService;
    private final NotificationService notificationService;

    @Autowired
    public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeService employeeService, NotificationService notificationService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeService = employeeService;
        this.notificationService = notificationService;
    }

    @Override
    public LeaveSummaryDTO getLeaveSummary(String username) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);

        List<LeaveRequest> allRequests = leaveRequestRepository.findByEmployeeOrderByAppliedOnDesc(employee);
        List<LeaveRequestDTO> dtoList = allRequests.stream().map(this::mapToDTO).collect(Collectors.toList());

        long pending = leaveRequestRepository.countByEmployeeAndStatus(employee, "PENDING");
        long approved = leaveRequestRepository.countByEmployeeAndStatus(employee, "APPROVED");
        long rejected = leaveRequestRepository.countByEmployeeAndStatus(employee, "REJECTED");

        int totalAllocated = 24; // Standard annual leave balance
        int available = (int) (totalAllocated - approved);
        if (available < 0) available = 0;

        return new LeaveSummaryDTO(available, pending, approved, rejected, dtoList);
    }

    @Override
    public LeaveRequestDTO applyLeave(String username, LeaveRequestDTO leaveRequestDTO) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);

        if (leaveRequestDTO.getStartDate() == null || leaveRequestDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required.");
        }

        if (leaveRequestDTO.getStartDate().isAfter(leaveRequestDTO.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        if (leaveRequestDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot apply leave for a past date.");
        }

        LeaveRequest leaveRequest = new LeaveRequest(
                employee,
                leaveRequestDTO.getLeaveType() != null ? leaveRequestDTO.getLeaveType() : "CASUAL",
                leaveRequestDTO.getStartDate(),
                leaveRequestDTO.getEndDate(),
                leaveRequestDTO.getReason(),
                "PENDING",
                LocalDate.now()
        );

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);

        notificationService.createNotification(
                employee,
                "Leave Application Submitted",
                "Your " + saved.getLeaveType() + " leave application from " + saved.getStartDate() + " to " + saved.getEndDate() + " has been submitted for approval.",
                "LEAVE"
        );

        return mapToDTO(saved);
    }

    @Override
    public void cancelLeave(String username, Long leaveId) {
        Employee employee = employeeService.getEmployeeEntityByUsername(username);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found with id: " + leaveId));

        if (!leaveRequest.getEmployee().getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Forbidden: You cannot cancel another employee's leave request.");
        }

        if (!"PENDING".equalsIgnoreCase(leaveRequest.getStatus())) {
            throw new IllegalArgumentException("Only PENDING leave requests can be cancelled.");
        }

        leaveRequest.setStatus("CANCELLED");
        leaveRequestRepository.save(leaveRequest);
    }

    private LeaveRequestDTO mapToDTO(LeaveRequest leaveRequest) {
        return new LeaveRequestDTO(
                leaveRequest.getId(),
                leaveRequest.getEmployee().getId(),
                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason(),
                leaveRequest.getStatus(),
                leaveRequest.getAppliedOn()
        );
    }
}
