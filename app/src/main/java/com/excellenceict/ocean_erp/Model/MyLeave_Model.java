package com.excellenceict.ocean_erp.Model;

public class MyLeave_Model {
    private String name;
    private String designation;
    private String department;
    private String leaveType;
    private String leaveAllowed;
    private String leaveCount;
    private String leaveBalance;

    public MyLeave_Model(String name, String designation, String department, String leaveType, String leaveAllowed, String leaveCount, String leaveBalance) {
        this.name = name;
        this.designation = designation;
        this.department = department;
        this.leaveType = leaveType;
        this.leaveAllowed = leaveAllowed;
        this.leaveCount = leaveCount;
        this.leaveBalance = leaveBalance;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartment() {
        return department;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getLeaveAllowed() {
        return leaveAllowed;
    }

    public String getLeaveCount() {
        return leaveCount;
    }

    public String getLeaveBalance() {
        return leaveBalance;
    }
}
