package com.example.tihomir_trifonov_employees;

public class CoupleEmployees {

    private String employerOneId;
    private String employerTwoId;
    private String projectId;
    private int workDays;
    private int sumWorkDays;


    public CoupleEmployees(String employerOneId, String employerTwoId, String projectId, int workDays, int sumWorkDays) {

        this.employerOneId = employerOneId;
        this.employerTwoId = employerTwoId;
        this.projectId = projectId;
        this.workDays = workDays;
        this.sumWorkDays = sumWorkDays;
    }

    public String getEmployerOneId() {
        return employerOneId;
    }
    public void setEmployerOneId(String employerOneId) {
        this.employerOneId = employerOneId;
    }


    public String getEmployerTwoId() {
        return employerTwoId;
    }
    public void setEmployerTwoId(String employerTwoId) {
        this.employerTwoId = employerTwoId;
    }

    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public int getWorkDays() {
        return workDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

    public int getSumWorkDays() {
        return sumWorkDays;
    }

    public void setSumWorkDays(int sumWorkDays) {
        this.sumWorkDays = sumWorkDays;
    }
}
