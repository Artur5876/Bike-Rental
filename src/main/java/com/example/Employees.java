package com.example;

public class Employees {
    int employeeID;
    String firstName;
    String lastName;
    String email;
    String jobPosition;
    String hireDate;
    double salary;
    int departmentNum;

    //constructor
    public Employees(int employeeID, String firstName, String lastName, String email, String jobPosition,
            String hireDate, double salary, int departmentNum) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.jobPosition = jobPosition;
        this.hireDate = hireDate;
        this.salary = salary;
        this.departmentNum = departmentNum;
    }

    //getters
    public int getEmployeeID() {
        return employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public String getHireDate() {
        return hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public int getDepartmentNum() {
        return departmentNum;
    }
}