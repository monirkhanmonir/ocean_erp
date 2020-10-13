package com.ocean.orcl;

public class AttendenceLog_Details_C_Entity {

    String dummyData;
    String time;
    String mode;
    String office_Location;

    public AttendenceLog_Details_C_Entity(String dummyData, String time, String mode, String office_Location) {
        this.dummyData = dummyData;
        this.time = time;
        this.mode = mode;
        this.office_Location = office_Location;
    }

    public String getDummyData() {
        return dummyData;
    }

    public String getTime() {
        return time;
    }

    public String getMode() {
        return mode;
    }

    public String getOffice_Location() {
        return office_Location;
    }
}
