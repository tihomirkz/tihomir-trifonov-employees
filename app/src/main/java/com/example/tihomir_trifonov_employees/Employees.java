package com.example.tihomir_trifonov_employees;

public class Employees {

    private String employerId;
    private String projectId;
    private String dateFrom;
    private String dateTo;



    public Employees(String employerId, String projectId, String dateFrom, String dateTo) {

        this.employerId = employerId;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;

    }

    public String getEmployerId() {
        return employerId;
    }
    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

}
