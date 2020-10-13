package com.ocean.orcl;

public class MyTeams_Entity {
    String person_no;
    String emp_name;
    String designation;
    String departmetn;
    String loginTime;
    String late_logInReason;
    String logoutTime;
    String early_logOutReason;
    String absent_Resason;
    String weekend;
    String holiday;
    String lateLogin_Flag;
    String earlyLogOut_Flag;

    public MyTeams_Entity(String person_no, String emp_name, String designation, String departmetn, String loginTime, String late_logInReason, String logoutTime, String early_logOutReason, String absent_Resason,String weekend,String holiday,String lateLogin_Flag,String earlyLogOut_Flag) {
        this.person_no = person_no;
        this.emp_name = emp_name;
        this.designation = designation;
        this.departmetn = departmetn;
        this.loginTime = loginTime;
        this.late_logInReason = late_logInReason;
        this.logoutTime = logoutTime;
        this.early_logOutReason = early_logOutReason;
        this.absent_Resason = absent_Resason;
        this.weekend = weekend;
        this.holiday = holiday;
        this.lateLogin_Flag = lateLogin_Flag;
        this.earlyLogOut_Flag = earlyLogOut_Flag;
    }

    public String getPerson_no() {
        return person_no;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartmetn() {
        return departmetn;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public String getLate_logInReason() {
        return late_logInReason;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public String getEarly_logOutReason() {
        return early_logOutReason;
    }

    public String getAbsent_Resason() {
        return absent_Resason;
    }

    public String getWeekend() {
        return weekend;
    }

    public String getHoliday() {
        return holiday;
    }

    public String getLateLogin_Flag() {
        return lateLogin_Flag;
    }

    public String getEarlyLogOut_Flag() {
        return earlyLogOut_Flag;
    }
}
