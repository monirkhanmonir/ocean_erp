package com.excellenceict.ocean_erp.Model;

import java.io.Serializable;

public class AttendenceLog_Model implements Serializable {
    private String persongID;
    private String persongName;
    private String persongDesignaton;
    private String personDepartment;
    private String persongLoginTime;
    private String lateDuration;
    private String personLogoutTime;
    private String earlyDuration;
    private String weekend;
    private String holiday;
    private String lateLogin_Flag;
    private String earlyLogOut_Flag;

    public AttendenceLog_Model(String persongID, String persongName, String persongDesignaton, String personDepartment, String persongLoginTime, String lateDuration, String personLogoutTime, String earlyDuration, String weekend, String holiday, String lateLogin_Flag, String earlyLogOut_Flag) {
        this.persongID = persongID;
        this.persongName = persongName;
        this.persongDesignaton = persongDesignaton;
        this.personDepartment = personDepartment;
        this.persongLoginTime = persongLoginTime;
        this.lateDuration = lateDuration;
        this.personLogoutTime = personLogoutTime;
        this.earlyDuration = earlyDuration;
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

    public String getLateDuration() {
        return lateDuration;
    }

    public String getPersonLogoutTime() {
        return personLogoutTime;
    }

    public String getEarlyDuration() {
        return earlyDuration;
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