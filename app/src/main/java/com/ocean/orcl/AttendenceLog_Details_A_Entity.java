package com.ocean.orcl;

public class AttendenceLog_Details_A_Entity {
    String empName;
    String empDesignation;
    String empDepartment;
//    String empMode;
//    String empDate;
//    String empTime;
//    String empLocation;


    public AttendenceLog_Details_A_Entity(String empName, String empDesignation, String empDepartment) {
        this.empName = empName;
        this.empDesignation = empDesignation;
        this.empDepartment = empDepartment;
    }

    public String getEmpName() {
        return empName;
    }

    public String getEmpDesignation() {
        return empDesignation;
    }

    public String getEmpDepartment() {
        return empDepartment;
    }
}
