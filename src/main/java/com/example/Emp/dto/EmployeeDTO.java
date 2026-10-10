package com.example.Emp.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Department is required")
    private String department;

    private String designation;
    private String employmentStatus;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    private Double salary;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String firstName, String lastName, String email, String phone, String department, Double salary, LocalDate joiningDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.designation = "Software Engineer";
        this.employmentStatus = "ACTIVE";
    }

    public EmployeeDTO(Long id, String firstName, String lastName, String email, String phone, String department, String designation, String employmentStatus, Double salary, LocalDate joiningDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.designation = designation;
        this.employmentStatus = employmentStatus;
        this.salary = salary;
        this.joiningDate = joiningDate;
    }

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation != null ? designation : "Software Engineer";
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmploymentStatus() {
        return employmentStatus != null ? employmentStatus : "ACTIVE";
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }
}