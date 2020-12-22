package com.excellenceict.ocean_erp;

public class MyAttendence_Entity {
    private String DATEs;
    private String LOGINTIME;
    private String LATE_LOGIN_REASON;
    private String LOGOUTTIME;
    private String EARLY_LOGOUT_REASON;
    private String ABSENT_REASON;
    private String weekend;
    private String holiday;
    private String lateLogin_Flag;
    private String earlyLogOut_Flag;

    public MyAttendence_Entity(String DATEs, String LOGINTIME, String LATE_LOGIN_REASON, String LOGOUTTIME, String EARLY_LOGOUT_REASON, String ABSENT_REASON,String weekend,String holiday,String lateLogin_Flag,String earlyLogOut_Flag) {
        this.DATEs = DATEs;
        this.LOGINTIME = LOGINTIME;
        this.LATE_LOGIN_REASON = LATE_LOGIN_REASON;
        this.LOGOUTTIME = LOGOUTTIME;
        this.EARLY_LOGOUT_REASON = EARLY_LOGOUT_REASON;
        this.ABSENT_REASON = ABSENT_REASON;
        this.weekend =weekend;
        this.holiday=holiday;
        this.lateLogin_Flag=lateLogin_Flag;
        this.earlyLogOut_Flag =earlyLogOut_Flag;

    }

    public String getDATEs() {
        return DATEs;
    }

    public String getLOGINTIME() {
        return LOGINTIME;
    }

    public String getLATE_LOGIN_REASON() {
        return LATE_LOGIN_REASON;
    }

    public String getLOGOUTTIME() {
        return LOGOUTTIME;
    }

    public String getEARLY_LOGOUT_REASON() {
        return EARLY_LOGOUT_REASON;
    }

    public String getABSENT_REASON() {
        return ABSENT_REASON;
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
