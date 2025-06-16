package com.example;

public class Jobs {
    int jobId;
    int employerId;
    String jobTitle;
    String jobDescription;
    String department;
    String location;
    String closingDate;
    String postedDate;
    String status;

    //constructor
    public Jobs(int jobId, int employerId, String jobTitle, String jobDescription, String department, String location,
            String closingDate, String postedDate, String status) {
        this.jobId = jobId;
        this.employerId = employerId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.department = department;
        this.location = location;
        this.closingDate = closingDate;
        this.postedDate = postedDate;
        this.status = status;
        
    }

    //getters
    public int getJobId () {
        return jobId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getDepartment() {
        return department;
    }

    public String getLocation() {
        return location;
    }

    
    public String getPostedDate() {
        return postedDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public String getStatus() {
        return status;
    }
}