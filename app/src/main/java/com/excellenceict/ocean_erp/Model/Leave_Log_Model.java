package com.excellenceict.ocean_erp.Model;

import java.io.Serializable;

public class Leave_Log_Model implements Serializable {
    String id;
    String nameF;
    String nameL;
    String designation;
    String department;

    public Leave_Log_Model(String id, String nameF, String nameL, String designation, String department) {
        this.id = id;
        this.nameF = nameF;
        this.nameL = nameL;
        this.designation = designation;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public String getNameF() {
        return nameF;
    }

    public String getNameL() {
        return nameL;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDepartment() {
        return department;
    }
}