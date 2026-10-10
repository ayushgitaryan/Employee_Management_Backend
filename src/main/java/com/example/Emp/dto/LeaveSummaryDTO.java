package com.example.Emp.dto;

import java.util.List;

public class LeaveSummaryDTO {
    private int availableBalance;
    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;
    private List<LeaveRequestDTO> recentRequests;

    public LeaveSummaryDTO() {
    }

    public LeaveSummaryDTO(int availableBalance, long pendingCount, long approvedCount, long rejectedCount, List<LeaveRequestDTO> recentRequests) {
        this.availableBalance = availableBalance;
        this.pendingCount = pendingCount;
        this.approvedCount = approvedCount;
        this.rejectedCount = rejectedCount;
        this.recentRequests = recentRequests;
    }

    public int getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(int availableBalance) { this.availableBalance = availableBalance; }

    public long getPendingCount() { return pendingCount; }
    public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }

    public long getApprovedCount() { return approvedCount; }
    public void setApprovedCount(long approvedCount) { this.approvedCount = approvedCount; }

    public long getRejectedCount() { return rejectedCount; }
    public void setRejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; }

    public List<LeaveRequestDTO> getRecentRequests() { return recentRequests; }
    public void setRecentRequests(List<LeaveRequestDTO> recentRequests) { this.recentRequests = recentRequests; }
}
