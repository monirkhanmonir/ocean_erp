package com.ocean.orcl;

public class AttendenceLog_Details_B_Entity {
    String weekendDay;
    String holiDay;
    String login_Time;
    String login_Office_Location;
    String required_Login_Time;
    String late_Login;
    String late_Login_Reason;
    String logOut_Time;
    String logOut_Office_Location;
    String required_Logout_Time;
    String early_Logout;
    String early_Logout_Reason;
    String absent;
    String absentReason;

    public AttendenceLog_Details_B_Entity(String weekendDay, String holiDay, String login_Time, String login_Office_Location, String required_Login_Time, String late_Login, String late_Login_Reason, String logOut_Time, String logOut_Office_Location, String required_Logout_Time, String early_Logout, String early_Logout_Reason, String absent, String absentReason) {
        this.weekendDay = weekendDay;
        this.holiDay = holiDay;
        this.login_Time = login_Time;
        this.login_Office_Location = login_Office_Location;
        this.required_Login_Time = required_Login_Time;
        this.late_Login = late_Login;
        this.late_Login_Reason = late_Login_Reason;
        this.logOut_Time = logOut_Time;
        this.logOut_Office_Location = logOut_Office_Location;
        this.required_Logout_Time = required_Logout_Time;
        this.early_Logout = early_Logout;
        this.early_Logout_Reason = early_Logout_Reason;
        this.absent = absent;
        this.absentReason = absentReason;
    }

    public String getWeekendDay() {
        return weekendDay;
    }

    public String getHoliDay() {
        return holiDay;
    }

    public String getLogin_Time() {
        return login_Time;
    }

    public String getLogin_Office_Location() {
        return login_Office_Location;
    }

    public String getRequired_Login_Time() {
        return required_Login_Time;
    }

    public String getLate_Login() {
        return late_Login;
    }

    public String getLate_Login_Reason() {
        return late_Login_Reason;
    }

    public String getLogOut_Time() {
        return logOut_Time;
    }

    public String getLogOut_Office_Location() {
        return logOut_Office_Location;
    }

    public String getRequired_Logout_Time() {
        return required_Logout_Time;
    }

    public String getEarly_Logout() {
        return early_Logout;
    }

    public String getEarly_Logout_Reason() {
        return early_Logout_Reason;
    }

    public String getAbsent() {
        return absent;
    }

    public String getAbsentReason() {
        return absentReason;
    }
}
