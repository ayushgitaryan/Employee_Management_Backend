package com.example.Emp.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;
    private String department;
    private String designation;
    private String employmentStatus;
    private Double salary;
    private LocalDate joiningDate;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, String phone, String department, Double salary, LocalDate joiningDate) {
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

    public Employee(String firstName, String lastName, String email, String phone, String department, String designation, String employmentStatus, Double salary, LocalDate joiningDate) {
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
