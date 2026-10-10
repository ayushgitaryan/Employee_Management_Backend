package com.example.Emp.dto;

public class AttendanceSummaryDTO {
    private String month;
    private long presentDays;
    private long absentDays;
    private long totalWorkingDays;

    public AttendanceSummaryDTO() {
    }

    public AttendanceSummaryDTO(String month, long presentDays, long absentDays, long totalWorkingDays) {
        this.month = month;
        this.presentDays = presentDays;
        this.absentDays = absentDays;
        this.totalWorkingDays = totalWorkingDays;
    }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public long getPresentDays() { return presentDays; }
    public void setPresentDays(long presentDays) { this.presentDays = presentDays; }

    public long getAbsentDays() { return absentDays; }
    public void setAbsentDays(long absentDays) { this.absentDays = absentDays; }

    public long getTotalWorkingDays() { return totalWorkingDays; }
    public void setTotalWorkingDays(long totalWorkingDays) { this.totalWorkingDays = totalWorkingDays; }
}
