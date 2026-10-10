package com.example.Emp.Service;

import com.example.Emp.dto.LeaveRequestDTO;
import com.example.Emp.dto.LeaveSummaryDTO;
import java.util.List;

public interface LeaveService {
    LeaveSummaryDTO getLeaveSummary(String username);
    LeaveRequestDTO applyLeave(String username, LeaveRequestDTO leaveRequestDTO);
    void cancelLeave(String username, Long leaveId);
}
