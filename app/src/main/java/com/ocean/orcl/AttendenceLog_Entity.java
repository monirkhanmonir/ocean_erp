package com.ocean.orcl;

import java.io.Serializable;

public class AttendenceLog_Entity implements Serializable {
    String persongID;
    String persongName;
    String persongDesignaton;
    String personDepartment;
    //    String personDate;
    String persongLoginTime;
    String personLogoutTime;
    String weekend;
    String holiday;
    String lateLogin_Flag;
    String earlyLogOut_Flag;


//    public AttendenceLog_Entity(String persongID, String persongName, String persongDesignaton, String personDepartment,String personDate, String persongLoginTime, String personLogoutTime) {

    public AttendenceLog_Entity(String persongID, String persongName, String persongDesignaton, String personDepartment, String persongLoginTime, String personLogoutTime,String weekend,String holiday, String lateLogin_Flag, String earlyLogOut_Flag) {
        this.persongID = persongID;
        this.persongName = persongName;
        this.persongDesignaton = persongDesignaton;
        this.personDepartment = personDepartment;
        this.persongLoginTime = persongLoginTime;
        this.personLogoutTime = personLogoutTime;
        this.weekend = weekend;
        this.holiday = holiday;
        this.lateLogin_Flag = lateLogin_Flag;
        this.earlyLogOut_Flag = earlyLogOut_Flag;
    }

    public String getPersongID() {
        return persongID;
    }

    public String getPersongName() {
        return persongName;
    }

    public String getPersongDesignaton() {
        return persongDesignaton;
    }

    public String getPersonDepartment() {
        return personDepartment;
    }

    public String getPersongLoginTime() {
        return persongLoginTime;
    }

    public String getPersonLogoutTime() {
        return personLogoutTime;
    }

    public String getLateLogin_Flag() {
        return lateLogin_Flag;
    }

    public String getEarlyLogOut_Flag() {
        return earlyLogOut_Flag;
    }

    public String getWeekend() {
        return weekend;
    }

    public String getHoliday() {
        return holiday;
    }
}